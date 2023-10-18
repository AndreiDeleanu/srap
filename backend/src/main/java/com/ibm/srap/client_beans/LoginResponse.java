package com.ibm.srap.client_beans;

import static com.ibm.srap.services.utils.SrapUtils.emptyIfNull;

import java.util.ArrayList;
import java.util.List;

public class LoginResponse extends DefaultResponse {
	
	private String token;
	private String user;
	private String userImageUrl;
	private List<Integer> domainId;
	private List<String> roles;
	private List<RoleMappingDO> roleMappings;
	private List<DelegationInfoDO> delegations;
	

	public LoginResponse(final String t) { token = t; }
	
	
	public String getToken() { return emptyIfNull(token); }
	public void setToken(String value) { token = value; }

	public String getUser() { return emptyIfNull(user); }
	public void setUser(String u) { user = u; }

	public List<String> getRoles() {
		if (roles == null) roles = new ArrayList<>();
		return roles;
	}
	public void setRoles(List<String> r) { roles = r; }

	public String getUserImageUrl() { return emptyIfNull(userImageUrl); }
	public void setUserImageUrl(String img) { userImageUrl = img; }
	
	public List<Integer> getDomainId() { return domainId; }
	public void setDomainId(List<Integer> d) { domainId = d;}

	public List<RoleMappingDO> getRoleMappings() {
		if (roleMappings == null) roleMappings = new ArrayList<>();
		return roleMappings;
	}
	public void setRoleMappings(List<RoleMappingDO> rm) { roleMappings = rm; }

	public List<DelegationInfoDO> getDelegations() { 
		if (delegations == null) delegations = new ArrayList<>();
		return delegations;
	}
	public void setDelegations(List<DelegationInfoDO> d) { delegations = d; }

}
