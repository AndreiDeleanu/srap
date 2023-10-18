package com.ibm.srap.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ibm.srap.models.Status;

public interface Statuses extends JpaRepository<Status, Integer> {
	
	/**
	 * @param name
	 * @return
	 */
	Status findOneByNameIgnoreCase(String name);
	
	/**
	 * @param name
	 * @return
	 */
	@Query("SELECT s.id FROM Status s WHERE s.name = :name")
	Integer findIdByName(@Param("name") String name);

}
