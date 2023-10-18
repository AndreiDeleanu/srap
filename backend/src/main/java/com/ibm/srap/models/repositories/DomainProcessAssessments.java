package com.ibm.srap.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ibm.srap.models.DomainProcessAssessment;

public interface DomainProcessAssessments extends JpaRepository<DomainProcessAssessment, Integer> {
	
	/**
	 * @param domainReportId The domain report ID in which a process assessment is made  
	 * @return The domain process assessments
	 */
	List<DomainProcessAssessment> findAllByReportId(Integer domainReportId);
	
	/**
	 * @param process The process ID for which an assessment is made
	 * @param domainReportId The domain report ID in which a process assessment is made
	 * @return The domain process assessment entity
	 */
	DomainProcessAssessment findOneByProcessIdAndReportId(Integer process, Integer domainReportId);

}
