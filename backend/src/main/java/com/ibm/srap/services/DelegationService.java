package com.ibm.srap.services;

import java.util.List;

import com.ibm.srap.client_beans.DelegationDO;
import com.ibm.srap.client_beans.DelegationInfoDO;
import com.ibm.srap.client_beans.OperationResult;

public interface DelegationService {
	
	List<DelegationDO> getDelegations(String input);

	OperationResult createDelegation(DelegationDO input);

	OperationResult updateDelegation(DelegationDO input);

	OperationResult deleteDelegation(DelegationDO input);

	List<DelegationInfoDO> getForDelegate(String input);

}
