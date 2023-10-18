package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.*;

public class SubdomainReportSummaryDO {
	
	private Integer subdomainReportId;
	private Integer subdomainId;
	private String subdomainName;
	private Double fllAverage;
	private Double calculated;
	private String sllOverride;
	private String sllEmail;
	
	
	public Integer getSubdomainReportId() { return zeroIfNull(subdomainReportId); }
	public void setSubdomainReportId(Integer id) { subdomainReportId = id; }
	
	public Integer getSubdomainId() { return zeroIfNull(subdomainId); }
	public void setSubdomainId(Integer id) { subdomainId = id; }
	
	public String getSubdomainName() { return emptyIfNull(subdomainName); }
	public void setSubdomainName(String name) { subdomainName = name; }
	
	public Double getFllAverage() { return zeroIfNull(fllAverage); }
	public void setFllAverage(Double val) { fllAverage = val; }
	
	public Double getCalculated() {	return zeroIfNull(calculated); }
	public void setCalculated(Double val) { calculated = val; }
	
	public String getSllOverride() { return emptyIfNull(sllOverride); }
	public void setSllOverride(String name) { sllOverride = name; }
	
	public String getSllEmail() { return emptyIfNull(sllEmail); }
	public void setSllEmail(String name) { sllEmail = name; }

}
