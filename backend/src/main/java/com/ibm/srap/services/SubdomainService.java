package com.ibm.srap.services;

import java.util.List;

import com.ibm.srap.client_beans.OperationResult;
import com.ibm.srap.client_beans.SubdomainDO;

public interface SubdomainService {
	
	List<SubdomainDO> getAllForDomain(Integer domainId);
	
	OperationResult createNewSubdomain(SubdomainDO bean);
	
	OperationResult updateSubdomain(SubdomainDO bean);

}
