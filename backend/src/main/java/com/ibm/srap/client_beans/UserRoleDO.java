package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.*;

public class UserRoleDO extends IdNameDO {
	
	private String role;
	private String user;
	private Integer domainId;
	
	
	public String getRole() { return emptyIfNull(role); }
	public void setRole(String r) { role = r; }
	
	public String getUser() { return emptyIfNull(user); }
	public void setUser(String u) { user = u; }
	
	public Integer getDomainId() { return domainId; }
	public void setDomainId(Integer d) { domainId = d; }

}
