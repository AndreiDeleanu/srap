/**
 * 
 */
package com.ibm.srap.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ibm.srap.models.Threshold;

/**
 * @author AndreiDeleanu
 *
 */
public interface Thresholds extends JpaRepository<Threshold, Integer> {

	/**
	 * @param domainId The domain ID of threshold
	 * @return list of thresholds data by domain ID
	 */
	List<Threshold> findAllByDomainId(Integer domainId);

	/**
	 * @param domainId The domain ID of threshold
	 * @param quarter  The quarter in which threshold is created
	 * @param year     The year in which threshold is created
	 * @return threshold An threshold entity by domain ID, quarter and year
	 */
	Threshold findByDomainIdAndQuarterAndYear(Integer domainId, String quarter, String year);

	/**
	 * @param domainId The domain ID of threshold
	 * @param quarter  The quarter in which threshold is created
	 * @param year     The year in which threshold is created
	 * @return Long Returns number of tresholds by domain ID, current quarter and
	 *         year.
	 */
	Long countByDomainIdAndQuarterAndYear(Integer domainId, String quarter, String year);

	/**
	 * @param id      The ID of threshold
	 * @param quarter The quarter in which threshold is created
	 * @param year    The year in which threshold is created
	 * @return threshold An threshold entity by quarter and year
	 */
	Threshold findByIdAndQuarterAndYear(Integer id, String quarter, String year);

	/**
	 * @param domainId The domain ID of threshold
	 * @param quarter  The quarter in which threshold is created
	 * @param year     The year in which threshold is created
	 * @return threshold An threshold entity by domain ID, quarter and year
	 */

	@Query(value = "select * from {h-schema}THRESHOLD as t where ((ltrim(rtrim(t.year)) concat ltrim(rtrim(t.QUARTER)) <= :year concat :quarter) and (t.domain_id = :domainId)) order by (integer(ltrim(rtrim(t.year)) concat CASE when length(ltrim(rtrim(t.quarter))) = 1 then '0' concat ltrim(rtrim(t.quarter)) else ltrim(rtrim(t.quarter)) END)) DESC", nativeQuery = true)
	List<Threshold> findSpecificByDomainIdAndQuarterAndYear(@Param("domainId") Integer domainId,
			@Param("quarter") String quarter, @Param("year") String year);
}
