package com.amtrust.discount.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestParam;

import com.amtrust.discount.entity.DiscountCode;

public interface DiscountCodeRepository extends CrudRepository<DiscountCode, Long> {

	public List<DiscountCode> findByRecipientId(@RequestParam Long recipientId);

}
