package com.amtrust.discount.response;

import com.amtrust.discount.entity.Recipient;

import lombok.Data;

@Data
public class RecipientResponse extends Response{
	Recipient recipient;
}
