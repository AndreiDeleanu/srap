package com.ibm.srap.client_beans;

public class SchedulerInfoDO {
	
	private FpReportsTaskInfoDO taskInfoFPs;
	private FllReportsTaskInfoDO taskInfoForFLLs;
	private SLLReportsTaskInfoDO taskInfoForSLLs;
	
	
	public FpReportsTaskInfoDO getTaskInfoFPs() { return taskInfoFPs; }
	public void setTaskInfoForFPs(FpReportsTaskInfoDO t) { taskInfoFPs = t; }
	
	public FllReportsTaskInfoDO getTaskInfoForFLLs() { return taskInfoForFLLs; }
	public void setTaskInfoForFLLs(FllReportsTaskInfoDO t) { taskInfoForFLLs = t; }
	
	public SLLReportsTaskInfoDO getTaskInfoForSLLs() { return taskInfoForSLLs; }
	public void setTaskInfoForSLLs(SLLReportsTaskInfoDO t) { taskInfoForSLLs = t; }
	
}
