package com.ibm.srap.services;

import java.util.List;

import com.ibm.srap.client_beans.DomainReportDO;
import com.ibm.srap.client_beans.OperationResult;

public interface DomainReportService {
	
	OperationResult saveReport(DomainReportDO bean);

	List<DomainReportDO> getDomainReports(Integer domainId, String quarter);

}
