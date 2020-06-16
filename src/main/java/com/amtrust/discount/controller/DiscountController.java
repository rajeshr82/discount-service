package com.amtrust.discount.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amtrust.discount.constant.ServicesErrorCode;
import com.amtrust.discount.entity.DiscountCode;
import com.amtrust.discount.entity.Recipient;
import com.amtrust.discount.entity.SpecialOffer;
import com.amtrust.discount.exception.DiscountServiceException;
import com.amtrust.discount.request.RedeemDiscountCodeRequest;
import com.amtrust.discount.request.ValidateSpecialOfferRequest;
import com.amtrust.discount.response.DiscountCodeResponse;
import com.amtrust.discount.response.DiscountCodesResponse;
import com.amtrust.discount.response.GetDiscountCodeResponse;
import com.amtrust.discount.response.RecipientResponse;
import com.amtrust.discount.response.RecipientsResponse;
import com.amtrust.discount.response.RedeemCodeResponse;
import com.amtrust.discount.response.ResponseStatus;
import com.amtrust.discount.response.ResponseStatusConstants;
import com.amtrust.discount.response.SpecialOfferResponse;
import com.amtrust.discount.response.SpecialOffersResponse;
import com.amtrust.discount.response.ValidationResponse;
import com.amtrust.discount.service.DiscountService;
import com.amtrust.discount.utils.ValidatorUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "This controller handles all services related to Discounts codes allocated to recipients", tags = {
		"Discount Controller" })
@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
public class DiscountController {
	@Autowired
	DiscountService discountService;
	public static final String KEY = "THE_KEY";
	@Autowired
	ValidatorUtils validatorUtils;

	@CacheEvict(value = "recipients", allEntries = true)
	@ApiOperation(value = "This service helps adds a new recipient", response = RecipientResponse.class)
	@PostMapping(value = "/addRecipient", produces = { "application/json" })
	public RecipientResponse addRecipient(@RequestBody Recipient recipient) {
		RecipientResponse response = new RecipientResponse();
		ResponseStatus status = new ResponseStatus();
		try {
			// Validation Start -- Validating Request Object for Mandatory params
			ValidationResponse validationResponse = validatorUtils.isValid(recipient);
			if (!validationResponse.isValid()) {
				status.populateResponseStatus(ResponseStatusConstants.ERROR,
						ServicesErrorCode.INVALID_REQUEST.getErrorCode(), validationResponse.getErrorResponse());
				response.setResponseStatus(status);
				return response;
			}
			response.setRecipient(discountService.addRecipient(recipient));
			response.setResponseStatus(status);
		} catch (DiscountServiceException ex) {
			status.populateResponseStatus(ResponseStatusConstants.ERROR, ex.getFaultInfo().getErrorCode(),
					ex.getFaultInfo().getErrorDescription());
			response.setResponseStatus(status);
		}

		return response;
	}
	@CacheEvict(value = "specialOffers", allEntries = true)
	@ApiOperation(value = "This service helps adds a new Special Offer", response = SpecialOfferResponse.class)
	@PostMapping("/addSpecialOffer")
	public SpecialOfferResponse addSpecialOffer(@RequestBody SpecialOffer specialOffer) {
		SpecialOfferResponse response = new SpecialOfferResponse();
		ResponseStatus status = new ResponseStatus();
		try {
			// Validation Start -- Validating Request Object for Mandatory params
			ValidationResponse validationResponse = validatorUtils.isValid(specialOffer);
			if (!validationResponse.isValid()) {
				status.populateResponseStatus(ResponseStatusConstants.ERROR,
						ServicesErrorCode.INVALID_REQUEST.getErrorCode(), validationResponse.getErrorResponse());
				response.setResponseStatus(status);
				return response;
			}
			response.setSpecialOffer(discountService.addSpecialOffer(specialOffer));
			response.setResponseStatus(status);
		} catch (DiscountServiceException ex) {
			status.populateResponseStatus(ResponseStatusConstants.ERROR, ex.getFaultInfo().getErrorCode(),
					ex.getFaultInfo().getErrorDescription());
			response.setResponseStatus(status);
		}
		return response;
	}

	@CacheEvict(cacheNames = "specialOffers", allEntries = true)
	@ApiOperation(value = "This service deletes the requested Special Offer", response = ResponseStatus.class)
	@PostMapping("/deleteSpecialOffer")
	public ResponseStatus deleteSpecialOffer(@RequestParam("specialOfferId") Long specialOfferId) {
		ResponseStatus status = new ResponseStatus();
		try {
			discountService.deleteSpecialOffer(specialOfferId);
		} catch (DiscountServiceException ex) {
			status.populateResponseStatus(ResponseStatusConstants.ERROR, ex.getFaultInfo().getErrorCode(),
					ex.getFaultInfo().getErrorDescription());
		} catch (DataRetrievalFailureException ex) {
			status.populateResponseStatus(ResponseStatusConstants.ERROR,
					ServicesErrorCode.NO_DATA_AVAILABLE.getErrorCode(), ex.getMessage());
		}
		return status;
	}

	@CacheEvict(cacheNames = "recipients", allEntries = true)
	@ApiOperation(value = "This service deletes the requested Recipient", response = ResponseStatus.class)
	@PostMapping("/deleteRecipient")
	public ResponseStatus deleteRecipient(@RequestParam("recipientId") Long recipientId) {
		ResponseStatus status = new ResponseStatus();
		try {
			discountService.deleteRecipient(recipientId);
		} catch (DiscountServiceException ex) {
			status.populateResponseStatus(ResponseStatusConstants.ERROR, ex.getFaultInfo().getErrorCode(),
					ex.getFaultInfo().getErrorDescription());
		} catch (DataRetrievalFailureException ex) {
			status.populateResponseStatus(ResponseStatusConstants.ERROR,
					ServicesErrorCode.NO_DATA_AVAILABLE.getErrorCode(), ex.getMessage());
		}
		return status;
	}

	@Cacheable(cacheNames="recipients")
	@ApiOperation(value = "This service gives available list of all recipients", response = RecipientsResponse.class)
	@GetMapping(value = "/getRecipients", produces = { "application/json" })
	public RecipientsResponse getRecipients() {
		RecipientsResponse response = new RecipientsResponse();
		response.setRecipients(discountService.getRecipients());
		response.setResponseStatus(new ResponseStatus());
		System.out.println("getRecipients() Called!!");
		return response;
	}


	@Cacheable(cacheNames="specialOffers")
	@ApiOperation(value = "This service gives available Special offers accesible for all the recipients", response = SpecialOffersResponse.class)
	@GetMapping("/getSpecialOffers")
	public SpecialOffersResponse getSpecialOffers() {
		SpecialOffersResponse response = new SpecialOffersResponse();
		response.setSpecialOffers(discountService.getSpecialOffers());
		response.setResponseStatus(new ResponseStatus());
		System.out.println("getSpecialOffers() Called!!");
		return response;
	}
	

	@CacheEvict(cacheNames = {"specialOffers","recipients"}, allEntries = true)
	@GetMapping("/flushCache")
	public String flushCache() {
		System.out.println("Cache Flushed!!");
		
		return "Cache Flushed!!";
	}

	@ApiOperation(value = "This service gives available list of Discount Codes mapped to the recipients", response = DiscountCodesResponse.class)
	@GetMapping(value = "/getDiscountCodes", produces = { "application/json" })
	public DiscountCodesResponse getDiscountCodes() {
		List<DiscountCode> result = discountService.getDiscountCodes();
		DiscountCodesResponse response = new DiscountCodesResponse();

		response.setDiscountCodes(result);
		response.setResponseStatus(new ResponseStatus());
		return response;
	}

	@ApiOperation(value = "This service helps to validate and generates a new Discount Code mapped to Recipient", response = DiscountCodeResponse.class)
	@PostMapping(value = "/validateAndGetDiscountCode", produces = { "application/json" })
	public DiscountCodeResponse validateAndGetDiscountCode(
			@RequestBody ValidateSpecialOfferRequest validateSpecialOfferRequest) {
		// Validation Start -- Validating Request Object for Mandatory params
		ValidationResponse validationResponse = validatorUtils.isValid(validateSpecialOfferRequest);
		if (!validationResponse.isValid()) {
			DiscountCodeResponse response = new DiscountCodeResponse();
			ResponseStatus responseStatus = new ResponseStatus();
			responseStatus.populateResponseStatus(ResponseStatusConstants.ERROR,
					ServicesErrorCode.INVALID_REQUEST.getErrorCode(), validationResponse.getErrorResponse());
			response.setResponseStatus(responseStatus);
			return response;
		}
		return discountService.validateAndGetDiscountCode(validateSpecialOfferRequest);
	}

	@ApiOperation(value = "This service helps to check if user can redeem Discount Code based on Discount Code and Email", response = RedeemCodeResponse.class)
	@PostMapping(value = "/redeemDiscountCode", produces = { "application/json" })
	public RedeemCodeResponse redeemDiscountCode(@RequestBody RedeemDiscountCodeRequest validateDiscountRequest) {
		// Validation Start -- Validating Request Object for Mandatory params
		ValidationResponse validationResponse = validatorUtils.isValid(validateDiscountRequest);
		if (!validationResponse.isValid()) {
			RedeemCodeResponse response = new RedeemCodeResponse();
			ResponseStatus responseStatus = new ResponseStatus();
			responseStatus.populateResponseStatus(ResponseStatusConstants.ERROR,
					ServicesErrorCode.INVALID_REQUEST.getErrorCode(), validationResponse.getErrorResponse());
			response.setResponseStatus(responseStatus);
			return response;
		}

		return discountService.redeemDiscountCode(validateDiscountRequest);
	}

	@ApiOperation(value = "Get all the discount codes available for the requested Email")
	@PostMapping(value = "/getAllDiscountCodesByEmail", produces = { "application/json" })
	public GetDiscountCodeResponse getAllDiscountCodesByEmail(@RequestParam("email") String email) {
		return discountService.getAllDiscountCodesByEmail(email);
	}

}
