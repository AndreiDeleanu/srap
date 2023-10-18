package com.ibm.srap.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ibm.srap.models.SubdomainProcessAssessment;

public interface SubdomainProcessAssessments extends JpaRepository<SubdomainProcessAssessment, Integer> {
	
	/**
	 * @param subdomain
	 * @return
	 */
	List<SubdomainProcessAssessment> findAllByReportId(Integer subdomain);
	
	/**
	 * @param process
	 * @param domain
	 * @return
	 */
	SubdomainProcessAssessment findOneByProcessIdAndReportId(Integer process, Integer domain);

}
