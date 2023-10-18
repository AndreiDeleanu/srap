package com.ibm.srap.services;

import java.util.List;

import com.ibm.srap.client_beans.RoleMappingDO;

public interface UserRoleService {
	
	List<Integer> getDomainForUser(String user);

	List<String> lookupRoles(String userMail);
	
	List<RoleMappingDO> getRoleMappings(String user);

}
