package com.ibm.srap.services;

import com.ibm.srap.client_beans.NotificationDO;
import com.ibm.srap.client_beans.FllReportsTaskInfoDO;
import com.ibm.srap.client_beans.FpReportsTaskInfoDO;
import com.ibm.srap.client_beans.SLLReportsTaskInfoDO;

public interface SchedulerService {

	NotificationDO checkAndNotifyDomainAdmins();

	FpReportsTaskInfoDO checkAndSubmitSquadReports();

	FllReportsTaskInfoDO checkAndSignSquadReports();

	SLLReportsTaskInfoDO checkAndSubmitSubdomainReports();
	
	void storeRolesQuarterly();

}
