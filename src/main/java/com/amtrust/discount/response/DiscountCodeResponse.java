package com.amtrust.discount.response;

import com.amtrust.discount.entity.DiscountCode;

import lombok.Data;

@Data
public class DiscountCodeResponse extends Response{
	DiscountCode discountCode;
}
