package com.amtrust.discount.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.amtrust.discount.constant.ServicesErrorCode;
import com.amtrust.discount.entity.DiscountCode;
import com.amtrust.discount.entity.Recipient;
import com.amtrust.discount.entity.SpecialOffer;
import com.amtrust.discount.exception.DiscountServiceException;
import com.amtrust.discount.repository.DiscountCodeRepository;
import com.amtrust.discount.repository.RecipientRepository;
import com.amtrust.discount.repository.SpecialOfferRepository;
import com.amtrust.discount.request.RedeemDiscountCodeRequest;
import com.amtrust.discount.request.ValidateSpecialOfferRequest;
import com.amtrust.discount.response.DiscountCodeResponse;
import com.amtrust.discount.response.DiscountInfo;
import com.amtrust.discount.response.GetDiscountCodeResponse;
import com.amtrust.discount.response.RedeemCodeResponse;
import com.amtrust.discount.response.ResponseStatus;
import com.amtrust.discount.response.ResponseStatusConstants;
import com.amtrust.discount.utils.ValidatorUtils;

@Service
public class DiscountServiceImpl implements DiscountService {

	@Autowired
	RecipientRepository recipientRepository;

	@Autowired
	SpecialOfferRepository specialOfferRepository;

	@Autowired
	DiscountCodeRepository discountCodeRepository;

	@Autowired
	ValidatorUtils validatorUtils;

	@Override
	public SpecialOffer addSpecialOffer(SpecialOffer specialOffer) throws DiscountServiceException {
		return specialOfferRepository.save(specialOffer);
	}

	@Override
	public Recipient addRecipient(Recipient recipient) throws DiscountServiceException {
		return recipientRepository.save(recipient);
	}

	@Override
	public void deleteSpecialOffer(Long offerId) throws DiscountServiceException {
		specialOfferRepository.deleteById(offerId);
	}

	@Override
	public void deleteRecipient(Long recipientId) throws DiscountServiceException, DataRetrievalFailureException {
		recipientRepository.deleteById(recipientId);
	}

	@Override
	public List<SpecialOffer> getSpecialOffers() {
		Iterable<SpecialOffer> offers = specialOfferRepository.findAll();
		List<SpecialOffer> result = new ArrayList<SpecialOffer>();
		offers.forEach(result::add);
		return result;
	}

	@Override
	public List<Recipient> getRecipients() {
		Iterable<Recipient> recipients = recipientRepository.findAll();
		List<Recipient> result = new ArrayList<Recipient>();
		recipients.forEach(result::add);
		return result;
	}

	@Override
	public List<DiscountCode> getDiscountCodes() {
		Iterable<DiscountCode> recipients = discountCodeRepository.findAll();
		List<DiscountCode> result = new ArrayList<DiscountCode>();
		recipients.forEach(result::add);

		return result;
	}

	@Override
	public DiscountCodeResponse validateAndGetDiscountCode(ValidateSpecialOfferRequest validateSpecialOfferRequest) {
		DiscountCodeResponse response = new DiscountCodeResponse();
		DiscountCode discountCode = new DiscountCode();
		response.setDiscountCode(discountCode);
		Recipient r = recipientRepository.findByEmail(validateSpecialOfferRequest.getEmail());
		discountCode.setRecipient(r);
		SpecialOffer so = specialOfferRepository.findByOfferName(validateSpecialOfferRequest.getSpecialOfferName());
		discountCode.setSpecialOffer(so);
		// Validate Data
		ResponseStatus responseStatus = validatorUtils.validateSpecialOfferRequest(r, so);
		if (responseStatus.getStatus().equals(ResponseStatusConstants.ERROR)) {
			response.setResponseStatus(responseStatus);
			response.setDiscountCode(null);
			return response;
		}

		int hashcode = (r.hashCode() & 0xfffffff);
		String uniqueDiscountCode = hashcode + RandomStringUtils.randomAlphanumeric(8);
		discountCode.setDiscountCode(uniqueDiscountCode);
		discountCode.setExpiryDate(validateSpecialOfferRequest.getExpiryDate());
		discountCodeRepository.save(discountCode);
		response.setDiscountCode(discountCode);
		response.setResponseStatus(new ResponseStatus());

		return response;
	}

	@Override
	public RedeemCodeResponse redeemDiscountCode(RedeemDiscountCodeRequest validateDiscountRequest) {
		RedeemCodeResponse response = new RedeemCodeResponse();
		Recipient r = recipientRepository.findByEmail(validateDiscountRequest.getEmail());
		// Validate Data
		ResponseStatus responseStatus = validatorUtils.validateRedeemRequest(r, validateDiscountRequest);
		if (responseStatus.getStatus().equals(ResponseStatusConstants.ERROR)) {
			response.setResponseStatus(responseStatus);
			response.setDiscountPercent(0);
			return response;
		}
		if (!CollectionUtils.isEmpty(r.getDiscountCodes())) {
			DiscountCode dc = r.getDiscountCodes().stream()
					.filter(predicate -> validateDiscountRequest.getDiscountCode().equals(predicate.getDiscountCode()))
					.findAny().orElse(null);
			if (dc != null) {
				dc.setUsed(true);
				dc.setUsedDate(LocalDate.now());
				discountCodeRepository.save(dc);

				response.setDiscountPercent(dc.getSpecialOffer().getDiscountPercent());
			}
		}
		response.setResponseStatus(new ResponseStatus());

		return response;
	}

	public GetDiscountCodeResponse getAllDiscountCodesByEmail(String email) {
		GetDiscountCodeResponse response = new GetDiscountCodeResponse();
		ResponseStatus responseStatus = new ResponseStatus();
		Recipient r = recipientRepository.findByEmail(email);
		if (r != null && r.getDiscountCodes().size() > 0) {
			List<DiscountInfo> discountInfos = r.getDiscountCodes().stream()
					.map(discountCode -> new DiscountInfo(discountCode.getDiscountCode(),
							discountCode.getSpecialOffer().getOfferName(),discountCode.isUsed()))
					.collect(Collectors.toList());
			response.setDiscountInfos(discountInfos);
		} else if (r == null) {
			responseStatus.populateErrorResponseStatus(ServicesErrorCode.RECIPIENT_NOT_EXIST);
		}
		response.setResponseStatus(responseStatus);

		return response;
	}

}
