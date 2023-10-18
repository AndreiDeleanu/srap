package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.*;

import java.util.ArrayList;
import java.util.List;

public class DomainReportDO extends IdNameDO {
	
	private Integer domainReportId;
	private String doAssessment;
	private String doComment;
	private Double fllAverage;
	private String fllRating;
	private Double calculatedValue;
	private String quarter;
	private String year;
	private String projection;
	private List<ProcessAssessmentDO> processAssessments;
	private HistoryDO history;
	
	
	public Integer getDomainReportId() { return zeroIfNull(domainReportId); }
	public void setDomainReportId(Integer dr) { domainReportId = dr; }
	
	public String getDoAssessment() { return emptyIfNull(doAssessment); }
	public void setDoAssessment(String ovr) { doAssessment = ovr; }
	
	public String getDoComment() { return emptyIfNull(doComment); }
	public void setDoComment(String com) { doComment = com; }
	
	public Double getFllAverage() { return zeroIfNull(fllAverage); }
	public void setFllAverage(Double avg) { fllAverage = avg; }
	
	public String getFllRating() { return emptyIfNull(fllRating); }
	public void setFllRating(String f) { fllRating = f; }
	
	public List<ProcessAssessmentDO> getProcessAssessments() { 
		if (processAssessments == null) processAssessments = new ArrayList<>();
		return processAssessments; }
	public void setProcessAssessments(List<ProcessAssessmentDO> pa) { processAssessments = pa; }
	
	public Double getCalculatedValue() { return zeroIfNull(calculatedValue); }
	public void setCalculatedValue(Double val) { calculatedValue = val; }
	
	public String getQuarter() { return emptyIfNull(quarter); }
	public void setQuarter(String q) { quarter = q; }
	
	public String getYear() { return emptyIfNull(year); }
	public void setYear(String y) { year = y; }
	
	public String getProjection() {	return emptyIfNull(projection); }
	public void setProjection(String p) { projection = p; }
	
	public HistoryDO getHistory() { 
		if (history == null) history = new HistoryDO(); 
		return history; 
	}
	public void setHistory(HistoryDO obj) {	history = obj; }
	
}
