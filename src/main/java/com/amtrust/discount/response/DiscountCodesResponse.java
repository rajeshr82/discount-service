package com.amtrust.discount.response;

import java.util.ArrayList;
import java.util.List;

import com.amtrust.discount.entity.DiscountCode;

import lombok.Data;

@Data
public class DiscountCodesResponse extends Response {
	List<DiscountCode> discountCodes;

	public List<DiscountCode> getDiscountCodes() {
		if (discountCodes == null) {
			discountCodes = new ArrayList<DiscountCode>();
		}
		return discountCodes;
	}

	public void setDiscountCodes(List<DiscountCode> discountCodes) {
		this.discountCodes = discountCodes;
	}

}
