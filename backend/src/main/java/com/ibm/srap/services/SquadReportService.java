package com.ibm.srap.services;

import java.util.List;

import com.ibm.srap.client_beans.ReportStatusDO;
import com.ibm.srap.client_beans.SquadReportDO;
import com.ibm.srap.client_beans.CalculatedValuesGraphDO;
import com.ibm.srap.client_beans.EditSquadReportDO;
import com.ibm.srap.client_beans.FLLRatingGraphDO;
import com.ibm.srap.client_beans.ProgressGraphDO;

public interface SquadReportService {

	boolean updateReportStatus(ReportStatusDO bean);

	int createNewReport(EditSquadReportDO bean);

	boolean updateReport(EditSquadReportDO bean);

	List<SquadReportDO> getSquadsAndReportStatus(Integer domainId, String quarter);

	EditSquadReportDO getReport(int reportId);

	EditSquadReportDO getCurrentQuarterSquadReport(Integer squadId);

	FLLRatingGraphDO getFLLratingsGraph(Integer domainId);

	CalculatedValuesGraphDO getCalculatedRatingValuesGraph(Integer domainId);
	
	ProgressGraphDO getProgressGraphValue(Integer domainId);

}
