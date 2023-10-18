package com.ibm.srap.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ibm.srap.models.Domain;

public interface Domains extends JpaRepository<Domain, Integer> {
	
	/**
	 * @param name The name of a domain
	 * @return If a domain entity exists
	 */
	boolean existsByNameIgnoreCase(String name);
	
	/**
	 * @param user The name of the domain owner
	 * @param status The status of the domain
	 * @return If a domain entity exists
	 */
	boolean existsByOwnerAndStatusName(String user, String status);
	
	/**
	 * @param user The name of the domain admin
	 * @param status The status of the domain
	 * @return If a domain entity exists
	 */
	boolean existsByAdminAndStatusName(String user, String status);
	
	/**
	 * @param user The name of the domain owner
	 * @return The IDs of the active domain entities
	 */
	@Query("SELECT d.id FROM Domain d WHERE d.status.name = 'Active' AND d.owner = :user")
	List<Integer> getDomainsByOwner(@Param("user") String user);
	
	/**
	 * @param user The name of the domain admin
	 * @return The IDs of the active domain entities
	 */
	@Query("SELECT d.id FROM Domain d WHERE d.status.name = 'Active' AND d.admin = :user")
	List<Integer> getDomainsByAdmin(@Param("user") String user);
	
}
