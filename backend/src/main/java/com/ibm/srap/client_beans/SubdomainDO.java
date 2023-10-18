package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.*;

public class SubdomainDO extends IdNameDO {
	
	private String sllName;
	private Integer domainId;
	private String status;

	
	public String getSllName() { return emptyIfNull(sllName); }
	public void setSllName(String sll) { sllName = sll; }
	
	public Integer getDomainId() { return domainId; }
	public void setDomainId(Integer i) { domainId = i; }
	
	public String getStatus() { return emptyIfNull(status); }
	public void setStatus(String s) { status = s; } 

}
