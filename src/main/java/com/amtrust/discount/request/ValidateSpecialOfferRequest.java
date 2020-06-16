package com.amtrust.discount.request;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ValidateSpecialOfferRequest {

	@NotEmpty
	private String specialOfferName;

	@NotEmpty
	private String email;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd") 
	private LocalDate expiryDate;

}
