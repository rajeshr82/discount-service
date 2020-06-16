package com.amtrust.discount.service;

import java.util.List;

import com.amtrust.discount.entity.DiscountCode;
import com.amtrust.discount.entity.Recipient;
import com.amtrust.discount.entity.SpecialOffer;
import com.amtrust.discount.exception.DiscountServiceException;
import com.amtrust.discount.request.RedeemDiscountCodeRequest;
import com.amtrust.discount.request.ValidateSpecialOfferRequest;
import com.amtrust.discount.response.DiscountCodeResponse;
import com.amtrust.discount.response.GetDiscountCodeResponse;
import com.amtrust.discount.response.RedeemCodeResponse;

public interface DiscountService {

	public Recipient addRecipient(Recipient recipient) throws DiscountServiceException;

	public SpecialOffer addSpecialOffer(SpecialOffer specialOffer) throws DiscountServiceException;

	public void deleteSpecialOffer(Long offerId) throws DiscountServiceException;

	public void deleteRecipient(Long recipientId) throws DiscountServiceException;

	public List<SpecialOffer> getSpecialOffers();

	public List<DiscountCode> getDiscountCodes();

	public List<Recipient> getRecipients();

	public GetDiscountCodeResponse getAllDiscountCodesByEmail(String email);

	public DiscountCodeResponse validateAndGetDiscountCode(ValidateSpecialOfferRequest validateDiscountRequest);

	public RedeemCodeResponse redeemDiscountCode(RedeemDiscountCodeRequest validateDiscountRequest);

}
