package com.ibm.srap.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ibm.srap.models.ProcessAssessment;

public interface ProcessAssessments extends JpaRepository<ProcessAssessment, Integer> {
	
	/**
	 * @param reportId The squad report ID for which a process assessment is made
	 * @return The process assessments entities
	 */
	List<ProcessAssessment> findAllByReportId(Integer reportId);
	
	/**
	 * @param processId The process ID for which an assessment is made
	 * @param reportId The squad report ID for which a process assessment is made
	 * @return The process assessment entity
	 */
	ProcessAssessment findOneByProcessIdAndReportId(Integer processId, Integer reportId);
	
	/**
	 * @param processId The process ID for which an assessment is made
	 * @param domainId The domain ID in which the squad belongs
	 * @param quarter The quarter # (1, 2, 3, or 4)
	 * @param year The year, formatted as "YYYY"
	 * @return Rating values of the process assessments entities
	 */
	@Query("SELECT pa.rating.value FROM ProcessAssessment pa WHERE "
			+ "pa.process.id = :process AND "
			+ "pa.report.id IN "
			+ "(SELECT rep.id FROM SquadReport rep WHERE "
			+ "rep.squad.subdomain.domain.id = :domain AND " 
			+ "rep.quarter = :quarter AND "
			+ "rep.year = :year)")
	List<Integer> findAllCurrentProcessAssessments(
			@Param("process") Integer processId, @Param("domain") Integer domainId,
			@Param("quarter") String quarter, @Param("year") String year);
	
	/**
	 * @param processId The process ID for which an assessment is made
	 * @param subdomainId The subdomain ID in which the squad belongs
	 * @param quarter The quarter # (1, 2, 3, or 4)
	 * @param year The year, formatted as "YYYY"
	 * @return Rating values of the process assessments entities
	 */
	@Query("SELECT pa.rating.value FROM ProcessAssessment pa WHERE "
			+ "pa.process.id = :process AND "
			+ "pa.report.id IN "
			+ "(SELECT rep.id FROM SquadReport rep WHERE "
			+ "rep.squad.subdomain.id = :subdomain AND " 
			+ "rep.quarter = :quarter AND "
			+ "rep.year = :year)")
	List<Integer> findAllCurrentProcessAssessmentsBySubdomain(
			@Param("process") Integer processId, @Param("subdomain") Integer subdomainId,
			@Param("quarter") String quarter, @Param("year") String year);

}
