package com.ibm.srap.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ibm.srap.models.ProcessDashboard;

public interface ProcessesDashboard extends JpaRepository<ProcessDashboard, Integer> {
	
	
	/**
	 * @param domainId The domain ID in which a process is made
	 * @return list of processes for dashboard table data by domain id
	 */
	List<ProcessDashboard> findAllByDomainIdAndQuarterAndYear(Integer domainId, String quarter, String year);
}
