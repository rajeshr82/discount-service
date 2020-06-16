package com.amtrust.discount.response;

import lombok.Data;

@Data
public class DiscountInfo {

	private String discountCode;
	private String specialOfferName;
	private boolean isUsed;

	public DiscountInfo(String discountCode, String specialOfferName,boolean isUsed) {
		super();
		this.discountCode = discountCode;
		this.specialOfferName = specialOfferName;
		this.isUsed = isUsed;
	}

	public DiscountInfo() {
		super();
	}
}
