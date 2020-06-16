package com.amtrust.discount.response;

import java.util.List;

import com.amtrust.discount.entity.SpecialOffer;

import lombok.Data;

@Data
public class SpecialOffersResponse extends Response{
	List<SpecialOffer> specialOffers;
}
