package com.ibm.srap.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ibm.srap.models.Process;

public interface Processes extends JpaRepository<Process, Integer> {
	
	/**
	 * @param name The name of a process
	 * @return Process entities
	 */
	List<Process> findByName(String name);
	
	/**
	 * @param domainId The domain ID for which a process is assigned
	 * @return Process entities
	 */
	List<Process> findByDomainId(Integer domainId);
	
	/**
	 * @param domainId The domain ID for which a process is assigned
	 * @param statusId The status ID of a process
	 * @return Process entities
	 */
	List<Process> findByDomainIdAndStatusId(Integer domainId, Integer statusId);
	
}
