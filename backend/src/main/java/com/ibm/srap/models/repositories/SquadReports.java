package com.ibm.srap.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ibm.srap.models.SquadReport;
import com.ibm.srap.models.Status;

public interface SquadReports extends JpaRepository<SquadReport, Integer> {

	/**
	 * @param squadId The ID of the squad
	 * @return All squad reports entities for the given squad
	 */
	List<SquadReport> findAllBySquadId(Integer squadId);

	/**
	 * @param status The status of the squad report
	 * @return All squad reports entities with the given status
	 */
	List<SquadReport> findAllByStatus(Status status);

	/**
	 * @param squadId The ID of the squad
	 * @param quarter The quarter ("1", "2", "3", or "4")
	 * @param year    The year, formatted as "YYYY"
	 * @return The squad report entity
	 */
	SquadReport findOneBySquadIdAndQuarterAndYear(Integer squadId, String quarter, String year);

	/**
	 * @param squadId The ID of the squad
	 * @param quarter The quarter ("1", "2", "3", or "4")
	 * @param year    The year, formatted as "YYYY"
	 * @return If a quad report entity exists
	 */
	boolean existsBySquadIdAndQuarterAndYear(Integer squadId, String quarter, String year);

	/**
	 * @param domainId The ID of the domain in which the squad is assigned
	 * @param quarter  The quarter ("1", "2", "3", or "4")
	 * @param year     The year, formatted as "YYYY"
	 * @return All FLL rating values
	 */
	@Query("SELECT sr.fllRating.value FROM SquadReport sr WHERE " + "sr.squad.subdomain.domain.id = :domain AND "
			+ "sr.quarter = :quarter AND " + "sr.year = :year" + " AND sr.fllRating IS NOT NULL")
	List<Integer> findAllCurrentFLLRatingsByDomain(@Param("domain") Integer domainId, @Param("quarter") String quarter,
			@Param("year") String year);

	/**
	 * @param subdomainId The ID of the subdomain in which the squad is assigned
	 * @param quarter     The quarter ("1", "2", "3", or "4")
	 * @param year        The year, formatted as "YYYY"
	 * @return All FLL rating values
	 */
	@Query("SELECT sr.fllRating.value FROM SquadReport sr WHERE " + "sr.squad.subdomain.id = :subdomain AND "
			+ "sr.quarter = :quarter AND " + "sr.year = :year")
	List<Integer> findAllCurrentFLLRatingsBySubdomain(@Param("subdomain") Integer subdomainId,
			@Param("quarter") String quarter, @Param("year") String year);

	/**
	 * @param domainId The ID of the domain in which the squad is assigned
	 * @param quarter  The quarter ("1", "2", "3", or "4")
	 * @param year     The year, formatted as "YYYY"
	 * @return All calculated rating values
	 */
	@Query("SELECT sr.calculatedRating FROM SquadReport sr WHERE " + "sr.squad.subdomain.domain.id = :domain AND "
			+ "sr.quarter = :quarter AND " + "sr.year = :year" + " AND sr.calculatedRating IS NOT NULL")
	List<Double> findAllCalculatedValuesByDomain(@Param("domain") Integer domainId, @Param("quarter") String quarter,
			@Param("year") String year);

	/**
	 * @param quarter The quarter ("1", "2", "3", or "4")
	 * @param year    The year, formatted as "YYYY"
	 * @return The squad report entity
	 */
	List<SquadReport> findAllByQuarterAndYear(String quarter, String year);

}
