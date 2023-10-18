package com.ibm.srap.services;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ibm.srap.client_beans.OperationResult;
import com.ibm.srap.client_beans.ProcessDO;
import com.ibm.srap.client_beans.ProcessDashboardDO;
import com.ibm.srap.client_beans.SearchFilterParamsDO;

public interface ProcessService {

	List<ProcessDO> getDomainProcesses(Integer domainId);

	ProcessDO getProcessById(int id);

	boolean createNewProcess(ProcessDO pb);

	OperationResult updateProcess(ProcessDO pb);

	List<ProcessDashboardDO> getProcessesByDomainForCurrentQuarter(Integer domainId);

	XSSFWorkbook exportXLSX(SearchFilterParamsDO searchFilterParamsDO);

	List<ProcessDashboardDO> getProcessesByDomainIdAndFilters(SearchFilterParamsDO searchFilterParamsDO);

}
