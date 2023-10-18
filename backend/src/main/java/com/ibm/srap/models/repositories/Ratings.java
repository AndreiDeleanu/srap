package com.ibm.srap.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ibm.srap.models.Rating;

public interface Ratings extends JpaRepository<Rating, Integer> {
	
	/**
	 * @param value Rating value (-1, 0, 1 or 2)
	 * @return Rating entity
	 */
	Rating findOneByValue(Integer value);
	
	/**
	 * @param name The name of a rating
	 * @return Rating entity
	 */
	Rating findOneByName(String name);
	
}
