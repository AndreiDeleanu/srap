package com.ibm.srap.client_beans;

public class SubdomainProcessAssessmentDO {
	
	private Integer id;
	private String average;
	private Integer processId;
	private String processName;
	private Integer subdomainId;
	
	
	public Integer getId() { return id; }
	public void setId(Integer i) { id = i; }
	
	public String getAverage() { return average; }
	public void setAverage(String a) { average = a; }
	
	public Integer getProcessId() { return processId; }
	public void setProcessId(Integer p) { processId = p; }
	
	public Integer getSubdomainId() { return subdomainId; }
	public void setSubdomainId(Integer i) { subdomainId = i; }
	
	public String getProcessName() { return processName; }
	public void setProcessName(String p) { processName = p; }

}
