package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.emptyIfNull;

import java.util.ArrayList;
import java.util.List;

public class SubdomainReportDO extends IdNameDO {
	
	private String sllEmail;
	private Integer subdomainId;
	private String subdomainName;
	private Integer domainId;
	private String domainName;
	private String step;
	private String sllComment;
	private String sllOverride;
	private Double fllAverage;
	private Double calculatedValue;
	private String quarter;
	private String year;
	private List<SubdomainProcessAssessmentDO> processAssessments;
	private HistoryDO history;
	
	
	public String getSllEmail() { return emptyIfNull(sllEmail); }
	public void setSllEmail(String e) { sllEmail = e; }
	
	public Integer getSubdomainId() { return subdomainId; }
	public void setSubdomainId(Integer s) { subdomainId = s; }
	
	public String getSubdomainName() {return emptyIfNull(subdomainName); }
	public void setSubdomainName(String s) { subdomainName = s; }
	
	public String getStep() { return emptyIfNull(step); }
	public void setStep(String s) { step = s; }
	
	public Double getFllAverage() { return fllAverage; }
	public void setFllAverage(Double f) { fllAverage = f; }
	
	public Double getCalculatedValue() { return calculatedValue; }
	public void setCalculatedValue(Double c) { calculatedValue = c; } 
	
	public List<SubdomainProcessAssessmentDO> getProcessAssessments() { 
		if (processAssessments == null) processAssessments = new ArrayList<>();
		return processAssessments; }
	public void setProcessAssessments(List<SubdomainProcessAssessmentDO> pa) { processAssessments = pa; }
	
	public String getQuarter() { return emptyIfNull(quarter); }
	public void setQuarter(String q) { quarter = q; }
	
	public String getYear() { return emptyIfNull(year); }
	public void setYear(String y) { year = y; }
	
	public String getSllComment() { return emptyIfNull(sllComment); }
	public void setSllComment(String c) { sllComment = c; }
	
	public Integer getDomainId() { return domainId; }
	public void setDomainId(Integer d) { domainId = d; }
	
	public String getDomainName() { return emptyIfNull(domainName); }
	public void setDomainName(String d) { domainName = d; }
	
	public String getSllOverride() { return sllOverride; }
	public void setSllOverride(String o) { sllOverride = o;	}
	
	public HistoryDO getHistory() { 
		if (history == null) history = new HistoryDO(); 
		return history; 
	}
	public void setHistory(HistoryDO obj) {	history = obj; }

}
