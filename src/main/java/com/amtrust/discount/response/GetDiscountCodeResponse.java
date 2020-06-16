package com.amtrust.discount.response;

import java.util.List;

import lombok.Data;

@Data
public class GetDiscountCodeResponse extends Response {

	List<DiscountInfo> discountInfos;

	public List<DiscountInfo> getDiscountInfos() {
		return discountInfos;
	}

	public void setDiscountInfos(List<DiscountInfo> discountInfos) {
		this.discountInfos = discountInfos;
	}
}
