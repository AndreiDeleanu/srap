package com.ibm.srap.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ibm.srap.models.Deadline;

public interface Deadlines extends JpaRepository<Deadline, Integer> {
	
	/**
	 * @param domainId The domain ID for which a deadline is set
	 * @return All quarters deadlines
	 */
	List<Deadline> findAllByDomainId(Integer domainId);
	
	/**
	 * @param quarter A specific quarter, formatted as #QYYYY
	 * @return All deadlines in the given quarter
	 */
	List<Deadline> findAllByQuarter(String quarter);
	
	/**
	 * @param domainId The domain ID for which a deadline is set
	 * @param quarter A specific quarter, formatted as #QYYYY
	 * @return If a deadline is set
	 */
	boolean existsByDomainIdAndQuarter(Integer domainId, String quarter);
	
	/**
	 * @param domainId The domain ID for which a deadline is set
	 * @param quarter A specific quarter, formatted as #QYYYY
	 * @return The deadline entity
	 */
	Deadline findOneByDomainIdAndQuarter(Integer domainId, String quarter);
	
}
