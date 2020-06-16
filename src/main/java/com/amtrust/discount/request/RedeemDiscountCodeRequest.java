package com.amtrust.discount.request;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class RedeemDiscountCodeRequest {

	@NotEmpty
	private String discountCode;

	@NotEmpty
	private String email;
}
