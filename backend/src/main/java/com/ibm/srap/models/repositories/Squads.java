package com.ibm.srap.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ibm.srap.models.Squad;

public interface Squads extends JpaRepository<Squad, Integer> {

	/**
	 * @param user
	 * @return All squad entities where the user is mentioned as FLL or FP
	 */
	@Query("SELECT s FROM Squad s WHERE s.fll = :user OR s.fp = :user")
	List<Squad> findSquadFor(@Param("user") String user);

	/**
	 * @param domainId The domain ID to which the squad is assigned
	 * @return All squad entities in a given domain
	 */
	@Query("SELECT s FROM Squad s WHERE s.subdomain.domain.id = :domain")
	List<Squad> findByDomainId(@Param("domain") Integer domainId);

	/**
	 * @param user The username of an FLL
	 * @return The active domain ID in which the given person has the FLL role
	 */
	@Query("SELECT DISTINCT s.subdomain.domain.id FROM Squad s WHERE " + "s.status.name = 'Active' AND "
			+ "s.subdomain.domain.status.name = 'Active' AND " + "s.fll = :user")
	Integer getDomainByFll(@Param("user") String user);

	/**
	 * @param user The username of an FP
	 * @return The active domain ID in which the given person has the FP role
	 */
	@Query("SELECT DISTINCT s.subdomain.domain.id FROM Squad s WHERE " + "s.status.name = 'Active' AND "
			+ "s.subdomain.domain.status.name = 'Active' AND " + "s.fp = :user")
	Integer getDomainByFp(@Param("user") String user);

	/**
	 * @param user
	 * @param status
	 * @return If a squad entity exists with the given FLL user and squad status
	 */
	boolean existsByFllAndStatusName(String user, String status);

	/**
	 * @param user
	 * @param status
	 * @return If a squad entity exists with the given FP user and squad status
	 */
	boolean existsByFpAndStatusName(String user, String status);

	/**
	 * @param subdomainId
	 * @return If a squad entity exists for given subdomain ID
	 */
	boolean existsBySubdomainId(Integer subdomainId);

	/**
	 * @param year
	 * @param quarter
	 * @param domainId
	 * @return Count of active squads that do not have a report for current
	 *         quarter-year combination
	 */
	@Query(value = "SELECT count(*) FROM {h-schema}SQUADS as s left join {h-schema}SUBDOMAINS as d on s.SUBDOMAIN = d.id "
			+ "inner join {h-schema}DOMAINS as b on d.DOMAIN = b.id where s.status = 6  and b.id = :domainId and "
			+ "((select count(*) from {h-schema}REPORTS as r where s.id = r.squad and r.QUARTER = :quarter and r.YEAR = :year) = 0)", nativeQuery = true)
	Integer countUntouchedActiveSquadsByDomainId(@Param("domainId") Integer domainId, @Param("quarter") String quarter,
			@Param("year") String year);

	/**
	 * @param year
	 * @param quarter
	 * @param domainId
	 * @param reportStep
	 * @return Count of active squads by domain id, report step and current
	 *         quarter-year combination
	 */

	@Query(value = "SELECT count(*) FROM {h-schema}SQUADS as s left join {h-schema}SUBDOMAINS as d on s.SUBDOMAIN = d.id "
			+ "inner join {h-schema}DOMAINS as b on d.DOMAIN = b.id where s.status = 6  and b.id = :domainId and "
			+ "((select count(*) from {h-schema}REPORTS as r where s.id = r.squad and r.QUARTER = :quarter and r.YEAR = :year and r.status = :step) > 0)", nativeQuery = true)
	Integer countActiveSquadsByDomainIdReportsStepQuarterAndYear(@Param("domainId") Integer domainId,
			@Param("quarter") String quarter, @Param("year") String year, @Param("step") Integer reportStep);

	/**
	 * @param year
	 * @param quarter
	 * @param domainId
	 * @param maxThrehsold
	 * @param satThrehsold
	 * @return Count of active squads by domain id, report SAT and current
	 *         quarter-year combination
	 */

	@Query(value = "SELECT count(*) FROM {h-schema}SQUADS as s left join {h-schema}SUBDOMAINS as d on s.SUBDOMAIN = d.id "
			+ "inner join {h-schema}DOMAINS as b on d.DOMAIN = b.id where s.status = 6  and b.id = :domainId and "
			+ "((select count(*) from {h-schema}REPORTS as r where s.id = r.squad and r.QUARTER = :quarter and r.YEAR = :year and r.calculated_rating <= :maxThreshold and r.calculated_rating >= :satThreshold) = 1)", nativeQuery = true)
	Integer countSatActiveSquadsByDomainIdAndQuarterAndYear(@Param("domainId") Integer domainId,
			@Param("quarter") String quarter, @Param("year") String year, @Param("maxThreshold") Double maxThreshold,
			@Param("satThreshold") Double satThreshold);

	/**
	 * @param year
	 * @param quarter
	 * @param domainId
	 * @param marginalThreshold
	 * @param satThrehsold
	 * @return Count of active squads by domain id, report MARGINAL and current
	 *         quarter-year combination
	 */

	@Query(value = "SELECT count(*) FROM {h-schema}SQUADS as s left join {h-schema}SUBDOMAINS as d on s.SUBDOMAIN = d.id "
			+ "inner join {h-schema}DOMAINS as b on d.DOMAIN = b.id where s.status = 6  and b.id = :domainId and "
			+ "((select count(*) from {h-schema}REPORTS as r where s.id = r.squad and r.QUARTER = :quarter and r.YEAR = :year and r.calculated_rating < :satThreshold and r.calculated_rating >= :marginalThreshold) = 1)", nativeQuery = true)
	Integer countMarginalActiveSquadsByDomainIdAndQuarterAndYear(@Param("domainId") Integer domainId,
			@Param("quarter") String quarter, @Param("year") String year, @Param("satThreshold") Double satThreshold,
			@Param("marginalThreshold") Double marginalThreshold);

	/**
	 * @param year
	 * @param quarter
	 * @param domainId
	 * @param marginalThreshold
	 * @param minThreshold
	 * @return Count of active squads by domain id, report UNSAT and current
	 *         quarter-year combination
	 */

	@Query(value = "SELECT count(*) FROM {h-schema}SQUADS as s left join {h-schema}SUBDOMAINS as d on s.SUBDOMAIN = d.id "
			+ "inner join {h-schema}DOMAINS as b on d.DOMAIN = b.id where s.status = 6  and b.id = :domainId and "
			+ "((select count(*) from {h-schema}REPORTS as r where s.id = r.squad and r.QUARTER = :quarter and r.YEAR = :year and r.calculated_rating < :marginalThreshold and r.calculated_rating >= :minThreshold) = 1)", nativeQuery = true)
	Integer countUnsatActiveSquadsByDomainIdAndQuarterAndYear(@Param("domainId") Integer domainId,
			@Param("quarter") String quarter, @Param("year") String year,
			@Param("marginalThreshold") Double marginalThreshold, @Param("minThreshold") Double minThreshold);

	/**
	 * @param year
	 * @param quarter
	 * @param domainId
	 * @return Count of active squads UNCALCULATED rating by domain id, and current
	 *         quarter-year combination
	 */

	@Query(value = "SELECT count(*) FROM {h-schema}SQUADS as s left join {h-schema}SUBDOMAINS as d on s.SUBDOMAIN = d.id "
			+ "inner join {h-schema}DOMAINS as b on d.DOMAIN = b.id where s.status = 6  and b.id = :domainId and "
			+ "((select count(*) from {h-schema}REPORTS as r where s.id = r.squad and r.QUARTER = :quarter and r.YEAR = :year and r.calculated_rating is NULL) > 0)", nativeQuery = true)
	Integer countUncalculatedActiveSquadsByDomainIdAndQuarterAndYear(@Param("domainId") Integer domainId,
			@Param("quarter") String quarter, @Param("year") String year);
}
