package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.*;

public class ReportStatusDO {
	
	private Integer reportId;
	private String name;
	

	public Integer getReportId() { return reportId;	}
	public void setReportId(Integer reportId) { this.reportId = reportId; }
	
	public String getName() { return emptyIfNull(name); }
	public void setName(String name) { this.name = name; }

}
