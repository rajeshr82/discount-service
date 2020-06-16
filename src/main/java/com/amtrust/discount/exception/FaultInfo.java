package com.amtrust.discount.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FaultInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5218307542506381738L;

	private String errorCode;

	private String errorDescription;

	private String displayErrorMessage;

	private List<FieldErrorInfo> fieldErrors;

	public List<FieldErrorInfo> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<FieldErrorInfo> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

	public void addFieldError(FieldErrorInfo fieldErrorInfo) {
		if (fieldErrors == null) {
			fieldErrors = new ArrayList<FieldErrorInfo>();
		}
		fieldErrors.add(fieldErrorInfo);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public boolean hasErrors() {
		return this.fieldErrors != null && !this.fieldErrors.isEmpty();
	}

	public String getDisplayErrorMessage() {
		return displayErrorMessage;
	}

	public void setDisplayErrorMessage(String displayErrorMessage) {
		this.displayErrorMessage = displayErrorMessage;
	}

}
