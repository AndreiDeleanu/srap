package com.ibm.srap.client_beans;

import java.util.List;

public class RoleMappingDO {
	
	private Integer domainId;
	private List<String> roles;
	
	
	public RoleMappingDO(Integer domain, List<String> rolesList) {
		domainId = domain;
		roles = rolesList;
	}
	
	public Integer getDomainId() { return domainId; }
	public void setDomainId(Integer d) { domainId = d; }
	
	public List<String> getRoles() { return roles; }
	public void setRoles(List<String> r) { roles = r; }

}
