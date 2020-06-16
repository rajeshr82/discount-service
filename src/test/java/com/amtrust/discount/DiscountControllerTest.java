package com.amtrust.discount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.amtrust.discount.constant.ServicesErrorCode;
import com.amtrust.discount.entity.Recipient;
import com.amtrust.discount.entity.SpecialOffer;
import com.amtrust.discount.request.RedeemDiscountCodeRequest;
import com.amtrust.discount.request.ValidateSpecialOfferRequest;
import com.amtrust.discount.response.DiscountCodeResponse;
import com.amtrust.discount.response.DiscountCodesResponse;
import com.amtrust.discount.response.DiscountInfo;
import com.amtrust.discount.response.GetDiscountCodeResponse;
import com.amtrust.discount.response.RecipientResponse;
import com.amtrust.discount.response.RecipientsResponse;
import com.amtrust.discount.response.RedeemCodeResponse;
import com.amtrust.discount.response.ResponseStatus;
import com.amtrust.discount.response.ResponseStatusConstants;
import com.amtrust.discount.response.SpecialOffersResponse;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DiscountControllerTest {

	private static RestTemplate restTemplate = new RestTemplate();
	static String localHostUrl = "http://localhost:8080";
	static List<DiscountInfo> discountInfos;

	@Test(expected = HttpServerErrorException.class)
	public void addRecipient_null_email() {
		Recipient r = new Recipient();
		r.setEmail(null);
		r.setName("rajesh");
		RecipientResponse response = restTemplate.postForObject(localHostUrl + "/addRecipient", r, RecipientResponse.class);
		assertNotNull(response);
		assertNotNull(response.getRecipient());
		assertNotNull(response.getRecipient().getId());
	}

	@Test
	public void a_getAllDiscountCodesByEmail() {
		HttpEntity<MultiValueMap<String, Object>> requestEntity = populateMultimapRequest("email", "rajeshr82@gmail.com");
		GetDiscountCodeResponse response = restTemplate.postForObject(localHostUrl + "/getAllDiscountCodesByEmail",
				requestEntity, GetDiscountCodeResponse.class);
		assertNotNull(response);
		assertTrue(!CollectionUtils.isEmpty(response.getDiscountInfos()));
		discountInfos = response.getDiscountInfos();
		for(DiscountInfo di : discountInfos){
			assertTrue(StringUtils.isNotEmpty(di.getDiscountCode()));
			assertTrue(di.getDiscountCode().length() > 8);
			assertTrue(StringUtils.isNotEmpty(di.getSpecialOfferName()));
		}
	}

	// Redeem Discount Tests
	@Test
	public void b_redeemDiscountCode() {
		RedeemDiscountCodeRequest request = new RedeemDiscountCodeRequest();
		request.setEmail("rajesh@gmail.com");
		for(DiscountInfo discountInfo : discountInfos){
			request.setDiscountCode(discountInfo.getDiscountCode());
			RedeemCodeResponse response = restTemplate.postForObject(localHostUrl + "/redeemDiscountCode", request,
					RedeemCodeResponse.class);
			assertNotNull(response);
			assertNotNull(response.getDiscountPercent());
		}
	}
	
	@Test
	public void c_redeemDiscountCode_already_used() {
		RedeemDiscountCodeRequest request = new RedeemDiscountCodeRequest();
		request.setEmail("rajesh@gmail.com");
		for(DiscountInfo discountInfo : discountInfos){
			request.setDiscountCode(discountInfo.getDiscountCode());
			RedeemCodeResponse response = restTemplate.postForObject(localHostUrl + "/redeemDiscountCode", request,
					RedeemCodeResponse.class);
			assertNotNull(response);
			assertEquals(response.getResponseStatus().getErrorCode(),
					ServicesErrorCode.DISCOUNT_CODE_USED.getErrorCode());
		}
	}

	@Test
	public void d_redeemDiscountCode_invalid_discount_code() {
		RedeemDiscountCodeRequest request = new RedeemDiscountCodeRequest();
		request.setEmail("rajesh@gmail.com");
		request.setDiscountCode("Invalid code");
		RedeemCodeResponse response = restTemplate.postForObject(localHostUrl + "/redeemDiscountCode", request,
				RedeemCodeResponse.class);
		assertNotNull(response);
		assertEquals(response.getResponseStatus().getErrorCode(),
				ServicesErrorCode.DISCOUNT_CODE_NOT_MAPPED.getErrorCode());
	}

	@Test
	public void e_redeemDiscountCode_invalid_email() {
		RedeemDiscountCodeRequest request = new RedeemDiscountCodeRequest();
		request.setEmail("12131@gmail.com");
		request.setDiscountCode("Invalid code");
		RedeemCodeResponse response = restTemplate.postForObject(localHostUrl + "/redeemDiscountCode", request,
				RedeemCodeResponse.class);
		assertNotNull(response);
		assertEquals(response.getResponseStatus().getErrorCode(), ServicesErrorCode.RECIPIENT_NOT_EXIST.getErrorCode());
	}
	
	@Test
	public void f_redeemDiscountCode_expired_discount_code() {
		// Generate a expired discount code
		ValidateSpecialOfferRequest request = new ValidateSpecialOfferRequest();
		request.setEmail("rajesh@gmail.com");
		request.setSpecialOfferName("Discount40");
		request.setExpiryDate(LocalDate.now().minusDays(2));
		DiscountCodeResponse response = restTemplate.postForObject(localHostUrl + "/validateAndGetDiscountCode",
				request, DiscountCodeResponse.class);
		assertNotNull(response);
		
		RedeemDiscountCodeRequest request1 = new RedeemDiscountCodeRequest();
		request1.setEmail("rajesh@gmail.com");
		request1.setDiscountCode(response.getDiscountCode().getDiscountCode());
		RedeemCodeResponse response1 = restTemplate.postForObject(localHostUrl + "/redeemDiscountCode", request1,
				RedeemCodeResponse.class);
		assertNotNull(response1);
		assertEquals(response1.getResponseStatus().getErrorCode(),
				ServicesErrorCode.DISCOUNT_CODE_EXPIRED.getErrorCode());
	}

	// Validate Discount Code Tests
	@Test
	public void validateAndGetDiscountCode_already_mapped() {
		ValidateSpecialOfferRequest request = new ValidateSpecialOfferRequest();
		request.setEmail("rajesh@gmail.com");
		request.setSpecialOfferName("Discount10");
		DiscountCodeResponse response = restTemplate.postForObject(localHostUrl + "/validateAndGetDiscountCode",
				request, DiscountCodeResponse.class);
		assertNotNull(response);
		assertEquals(response.getResponseStatus().getErrorCode(),
				ServicesErrorCode.ALREADY_OFFER_MAPPED.getErrorCode());
	}

	@Test
	public void validateAndGetDiscountCode_null_email() {
		ValidateSpecialOfferRequest request = new ValidateSpecialOfferRequest();
		request.setEmail(null);
		request.setSpecialOfferName("Discount10");
		DiscountCodeResponse response = restTemplate.postForObject(localHostUrl + "/validateAndGetDiscountCode",
				request, DiscountCodeResponse.class);
		assertNotNull(response);
		assertEquals(response.getResponseStatus().getErrorCode(), ServicesErrorCode.INVALID_REQUEST.getErrorCode());
	}

	@Test
	public void validateAndGetDiscountCode_null_offer() {
		ValidateSpecialOfferRequest request = new ValidateSpecialOfferRequest();
		request.setEmail("rajesh@gmail.com");
		request.setSpecialOfferName(null);
		DiscountCodeResponse response = restTemplate.postForObject(localHostUrl + "/validateAndGetDiscountCode",
				request, DiscountCodeResponse.class);
		assertNotNull(response);
		assertEquals(response.getResponseStatus().getErrorCode(), ServicesErrorCode.INVALID_REQUEST.getErrorCode());
	}

	@Test
	public void validateAndGetDiscountCode_null_request() {
		ValidateSpecialOfferRequest request = new ValidateSpecialOfferRequest();
		request.setEmail(null);
		request.setSpecialOfferName(null);
		DiscountCodeResponse response = restTemplate.postForObject(localHostUrl + "/validateAndGetDiscountCode",
				request, DiscountCodeResponse.class);
		assertNotNull(response);
		assertEquals(response.getResponseStatus().getErrorCode(), ServicesErrorCode.INVALID_REQUEST.getErrorCode());
	}

	@Test
	public void validateAndGetDiscountCode_no_recipient() {
		ValidateSpecialOfferRequest request = new ValidateSpecialOfferRequest();
		request.setEmail("Junk Email");
		request.setSpecialOfferName("Discount10");
		DiscountCodeResponse response = restTemplate.postForObject(localHostUrl + "/validateAndGetDiscountCode",
				request, DiscountCodeResponse.class);
		assertNotNull(response);
		assertEquals(response.getResponseStatus().getErrorCode(), ServicesErrorCode.RECIPIENT_NOT_EXIST.getErrorCode());
	}

	@Test
	public void validateAndGetDiscountCode_no_offer() {
		ValidateSpecialOfferRequest request = new ValidateSpecialOfferRequest();
		request.setEmail("rajesh@gmail.com");
		request.setSpecialOfferName("Junk Discount");
		DiscountCodeResponse response = restTemplate.postForObject(localHostUrl + "/validateAndGetDiscountCode",
				request, DiscountCodeResponse.class);
		assertNotNull(response);
		assertEquals(response.getResponseStatus().getErrorCode(), ServicesErrorCode.OFFER_NOT_EXIST.getErrorCode());
	}
	
	// Deleting Data Tests
	@Ignore()
	public void z_deleteRecipients() {
		RecipientsResponse response = restTemplate.getForObject(localHostUrl + "/getRecipients",RecipientsResponse.class);
		assertNotNull(response);
		ResponseStatus status = null;
		HttpEntity<MultiValueMap<String, Object>> requestEntity = null;
		for(Recipient r : response.getRecipients()){
			requestEntity = populateMultimapRequest("recipientId", r.getId());
			status = restTemplate.postForObject(localHostUrl + "/deleteRecipient", requestEntity, ResponseStatus.class);
			assertEquals(ResponseStatusConstants.SUCCESS, status.getStatus());
		}
		// All Discount Offers also should be deleted when recipients are deleted
		DiscountCodesResponse codesResponse = restTemplate.getForObject(localHostUrl + "/getDiscountCodes",DiscountCodesResponse.class);
		assertTrue("Orphan Data is not DELETED!", codesResponse.getDiscountCodes().isEmpty());
	}
	
	@Ignore()
	public void z_deleteSpecialOffers() {
		SpecialOffersResponse response = restTemplate.getForObject(localHostUrl + "/getSpecialOffers",SpecialOffersResponse.class);
		assertNotNull(response);
		ResponseStatus status = null;
		HttpEntity<MultiValueMap<String, Object>> requestEntity = null;
		for(SpecialOffer so : response.getSpecialOffers()){
			requestEntity = populateMultimapRequest("specialOfferId", so.getId());
			status = restTemplate.postForObject(localHostUrl + "/deleteSpecialOffer", requestEntity, ResponseStatus.class);
			assertEquals(ResponseStatusConstants.SUCCESS, status.getStatus());
		}
		// All Discount Offers also should be deleted when recipients are deleted
		SpecialOffersResponse offersResponse = restTemplate.getForObject(localHostUrl + "/getSpecialOffers",SpecialOffersResponse.class);
		assertTrue("Orphan Data is not DELETED!", offersResponse.getSpecialOffers().isEmpty());
	}
	
	
	/**
	 * @return
	 */
	private HttpEntity<MultiValueMap<String, Object>> populateMultimapRequest(String param , Object value) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
		bodyMap.add(param, value);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
		return requestEntity;
	}

}
