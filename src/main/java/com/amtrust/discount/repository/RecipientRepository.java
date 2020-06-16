package com.amtrust.discount.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestParam;

import com.amtrust.discount.entity.Recipient;

public interface RecipientRepository extends CrudRepository<Recipient, Long> {

	Recipient findByEmail(@RequestParam("email") String email);
}
