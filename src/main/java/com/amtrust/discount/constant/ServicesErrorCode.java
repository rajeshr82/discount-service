package com.amtrust.discount.constant;

public enum ServicesErrorCode {
	SUCCESS(0, "Successfully Processed"), 
	UNABLE_TO_PROCESS_REQUEST(100,"Unable to process request"), 
	RECIPIENT_NOT_EXIST(101, "Recipient does not exist for the provided Email"),
	OFFER_NOT_EXIST(102, "Special Offer does not exist for provided Special Offer name"),
	ALREADY_OFFER_MAPPED(103, "Recipient is already mapped to this Special Offer"),
	DISCOUNT_CODE_USED(104, "Discount Code is already been used"),
	DISCOUNT_CODE_NOT_MAPPED(105, "Discount Code is not mapped to this User"),
	DISCOUNT_CODE_EXPIRED(106, "Discount Code is Expired"),
	RECIPIENT_NOT_MAPPED_TO_ANY_DISCOUNTS(107, "Recipient does not have any discounts mapped"),
	INVALID_REQUEST(108, "Input Request Invalid"),
	NO_DATA_AVAILABLE(109, "Data is unavailable for the request input")

	;

	private int errorCode;

	private String errorMessage;

	private static final String ERROR_CODE_PREFIX = "DMSERR";

	ServicesErrorCode(int errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return ERROR_CODE_PREFIX + this.errorCode;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
}
