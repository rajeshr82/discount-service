package com.amtrust.discount.exception;

import com.amtrust.discount.constant.ServicesErrorCode;

public class DiscountServiceException extends AppException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5762716594115074207L;

	public DiscountServiceException(String message) {
		super(message);
	}

	public DiscountServiceException(Exception ex) {
		super(ex);
	}

	public DiscountServiceException(ServicesErrorCode errorCode) {
		super(errorCode.getErrorMessage());
	}

	public DiscountServiceException(ServicesErrorCode errorCode, String displayMessage, Throwable ex) {
		super(ex);
	}

}
