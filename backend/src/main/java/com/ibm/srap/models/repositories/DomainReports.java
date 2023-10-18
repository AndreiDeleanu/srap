package com.ibm.srap.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ibm.srap.models.DomainReport;

public interface DomainReports extends JpaRepository<DomainReport, Integer> {
	
	/**
	 * @param domainId The domain ID for which a report is made 
	 * @param quarter The quarter # (1, 2, 3, or 4)
	 * @param year The year, formatted as "YYYY"
	 * @return The domain report entity
	 */
	DomainReport findOneByDomainIdAndQuarterAndYear(Integer domainId, String quarter, String year);
	
}
