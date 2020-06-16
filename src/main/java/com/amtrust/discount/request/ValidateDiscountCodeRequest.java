package com.amtrust.discount.request;

import javax.validation.constraints.NotEmpty;

public class ValidateDiscountCodeRequest {

	@NotEmpty
	private String discountCode;

	@NotEmpty
	private String email;

	public String getDiscountCode() {
		return discountCode;
	}

	public void setDiscountCode(String discountCode) {
		this.discountCode = discountCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "ValidateDiscountCodeRequest [discountCode=" + discountCode + ", email=" + email + "]";
	}

}
