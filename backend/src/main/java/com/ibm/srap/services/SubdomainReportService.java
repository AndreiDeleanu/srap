package com.ibm.srap.services;

import java.util.List;

import com.ibm.srap.client_beans.OperationResult;
import com.ibm.srap.client_beans.ReportStatusDO;
import com.ibm.srap.client_beans.SubdomainReportDO;
import com.ibm.srap.client_beans.SubdomainReportSummaryDO;

public interface SubdomainReportService {

	List<SubdomainReportDO> getSubdomainReportsFor(Integer subdomainId);
	
	List<SubdomainReportDO> getCurrentQuarterReportFor(Integer subdomainId);

	OperationResult saveReport(SubdomainReportDO bean);
	
	boolean updateReportStatus(ReportStatusDO bean);

	SubdomainReportDO getReport(int reportId);

	List<SubdomainReportSummaryDO> getReportsBySubdomain(Integer subdomainId, String quarter);

	List<SubdomainReportSummaryDO> getReportsByDomain(Integer domainId, String quarter);

}
