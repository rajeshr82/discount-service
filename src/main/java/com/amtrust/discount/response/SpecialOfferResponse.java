package com.amtrust.discount.response;

import com.amtrust.discount.entity.SpecialOffer;

import lombok.Data;

@Data
public class SpecialOfferResponse extends Response{
	SpecialOffer specialOffer;

}
