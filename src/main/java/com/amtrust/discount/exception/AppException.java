package com.amtrust.discount.exception;

import com.amtrust.discount.constant.ServicesErrorCode;

public class AppException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3108760172600375306L;

	private FaultInfo faultInfo;

	public FaultInfo getFaultInfo() {
		return faultInfo;
	}

	public void setFaultInfo(FaultInfo faultInfo) {
		this.faultInfo = faultInfo;
	}

	public AppException(String message) {
		super(message);
	}

	public AppException(String message, Throwable ex) {
		super(message, ex);
	}

	public AppException(Throwable ex) {
		super(ex);
	}

	protected void setFaultInfo(ServicesErrorCode errorCode, String displayMessage) {
		FaultInfo info = new FaultInfo();
		info.setDisplayErrorMessage(displayMessage);
		info.setErrorCode(errorCode.getErrorCode());
		info.setErrorDescription(errorCode.getErrorMessage());
		this.setFaultInfo(info);
	}

	protected void setFaultInfo(ServicesErrorCode errorCode, String displayMessage,
			boolean isDisplayMsg) {
		FaultInfo info = new FaultInfo();
		info.setDisplayErrorMessage(displayMessage);
		info.setErrorCode(errorCode.getErrorCode());
		if (isDisplayMsg) {
			info.setErrorDescription(displayMessage);
		}
		this.setFaultInfo(info);
	}

	protected void setFaultInfo(ServicesErrorCode errorCode, String dynamicMsg,
			String displayMessage) {
		FaultInfo info = new FaultInfo();
		info.setDisplayErrorMessage(displayMessage);
		info.setErrorCode(errorCode.getErrorCode());
		info.setErrorDescription(errorCode.getErrorMessage() + dynamicMsg);
		this.setFaultInfo(info);
	}
}
