package com.ibm.srap.services;

import java.util.List;

import com.ibm.srap.client_beans.DomainDO;
import com.ibm.srap.client_beans.OperationResult;

public interface DomainService {
	
	List<DomainDO> getAllDomains();
	
	DomainDO getDomainBy(Integer id);

	OperationResult createNewDomain(DomainDO domainBean);

	OperationResult updateDomain(DomainDO bean);

}
