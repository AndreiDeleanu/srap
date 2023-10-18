package com.ibm.srap.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ibm.srap.models.Status;
import com.ibm.srap.models.SubdomainReport;

public interface SubdomainReports extends JpaRepository<SubdomainReport, Integer> {

	/**
	 * @param subdomainId
	 * @return
	 */
	List<SubdomainReport> findAllBySubdomainId(Integer subdomainId);

	/**
	 * @param subdomainId
	 * @param quarter
	 * @param year
	 * @return
	 */
	List<SubdomainReport> findAllBySubdomainIdAndQuarterAndYear(Integer subdomainId, String quarter, String year);

	/**
	 * @param domainId
	 * @param quarter
	 * @param year
	 * @return
	 */
	List<SubdomainReport> findAllBySubdomainDomainIdAndQuarterAndYear(Integer domainId, String quarter, String year);

	/**
	 * @param status
	 * @return
	 */
	List<SubdomainReport> findAllByStatus(Status status);

	/**
	 * @param subdomainId
	 * @param quarter
	 * @param year
	 * @return
	 */
	SubdomainReport findOneBySubdomainIdAndQuarterAndYear(Integer subdomainId, String quarter, String year);

	/**
	 * @param quarter
	 * @param year
	 * @return
	 */
	List<SubdomainReport> findAllByQuarterAndYear(String quarter, String year);

}
