package com.ibm.srap.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ibm.srap.models.Subdomain;

public interface Subdomains extends JpaRepository<Subdomain, Integer> {
	
	/**
	 * @param domainId
	 * @return
	 */
	List<Subdomain> findByDomainId(Integer domainId);

	/**
	 * @param name
	 * @param domainId
	 * @return
	 */
	boolean existsByNameAndDomainId(String name, Integer domainId);
	
	/**
	 * @param user
	 * @param status
	 * @return
	 */
	boolean existsBySllAndStatusName(String user, String status);
	
	/**
	 * @param user
	 * @return
	 */
	@Query("SELECT DISTINCT s.domain.id FROM Subdomain s WHERE "
			+ "s.status.name = 'Active' AND "
			+ "s.domain.status.name = 'Active' AND "
			+ "s.sll = :user")
	Integer getDomainBySll(@Param("user") String user);
	
}
