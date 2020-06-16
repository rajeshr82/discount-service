package com.amtrust.discount.exception;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FieldErrorInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class FieldErrorInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1117088681398232851L;

	@XmlElement(name = "fieldName")
	private String fieldName;

	@XmlElement(name = "errorMessage")
	private String errorMessage;

	private static final String EMPTY_MESSAGE = "%s should not be empty";

	public FieldErrorInfo(String fieldName, String errorMessage) {
		this.fieldName = fieldName;
		this.errorMessage = errorMessage;
	}

	public FieldErrorInfo() {

	}

	public FieldErrorInfo addEmptyFieldMessage(String fieldName) {
		this.fieldName = fieldName;
		this.errorMessage = String.format(EMPTY_MESSAGE, fieldName);
		return this;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
