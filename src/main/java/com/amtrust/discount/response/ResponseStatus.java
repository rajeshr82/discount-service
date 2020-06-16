package com.amtrust.discount.response;

import java.io.Serializable;

import com.amtrust.discount.constant.ServicesErrorCode;

import lombok.Data;

@Data
public class ResponseStatus implements Serializable {

	private static final long serialVersionUID = -7788619177798333712L;

	private String status = ResponseStatusConstants.SUCCESS;
	private String errorCode;
	private String errorMessage;

	public void populateResponseStatus(String status, String errorCode, String errorMessage) {
		this.status = status;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public void populateErrorResponseStatus(ServicesErrorCode errorCode) {
		this.status = ResponseStatusConstants.ERROR;
		this.errorCode = errorCode.getErrorCode();
		this.errorMessage = errorCode.getErrorMessage();
	}

}