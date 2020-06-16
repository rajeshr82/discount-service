package com.amtrust.discount.response;

import java.util.List;

import com.amtrust.discount.entity.Recipient;

import lombok.Data;

@Data
public class RecipientsResponse extends Response{
	List<Recipient> recipients;
}
