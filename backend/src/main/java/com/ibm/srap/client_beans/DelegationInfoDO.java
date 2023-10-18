package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.*;

public class DelegationInfoDO {
	
	private Integer domainId;
	private String delegatorEmail;
	private String role;
	
	
	public Integer getDomainId() { return domainId; }
	public void setDomainId(Integer d) { domainId = d; }
	
	public String getDelegatorEmail() { return emptyIfNull(delegatorEmail); }
	public void setDelegatorEmail(String e) { delegatorEmail = e; }
	
	public String getRole() { return emptyIfNull(role); }
	public void setRole(String r) { role = r; }

}
