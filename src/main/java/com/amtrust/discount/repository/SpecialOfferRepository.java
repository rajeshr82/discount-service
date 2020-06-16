package com.amtrust.discount.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestParam;

import com.amtrust.discount.entity.SpecialOffer;

public interface SpecialOfferRepository extends CrudRepository<SpecialOffer, Long> {

	SpecialOffer findByOfferName(@RequestParam("offerName") String offerName);
}
