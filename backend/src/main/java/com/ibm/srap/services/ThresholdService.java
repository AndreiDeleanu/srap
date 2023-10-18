/**
 * 
 */
package com.ibm.srap.services;

import java.util.List;

import com.ibm.srap.client_beans.OperationResult;
import com.ibm.srap.client_beans.ThresholdDO;

/**
 * @author AndreiDeleanu
 *
 */
public interface ThresholdService {

	OperationResult createNewThreshold(ThresholdDO thresholdDO);

	ThresholdDO getThresholdsByDomainIdForCurrentQuarterAndYear(Integer domainId);

	ThresholdDO getSpecificThresholdByDomainIdQuarterYear(Integer domainId, String quarter, String year);

	OperationResult updateThreshold(ThresholdDO thresholdDO);

	List<ThresholdDO> getAllThresholdsByDomainId(Integer domainId);
}
