package com.ibm.srap.services;

import java.util.List;

import com.ibm.srap.client_beans.IdNameDO;

public interface RoleService {
	
	List<IdNameDO> getAllRoles();

	List<IdNameDO> getRolesByName(String name);
	
	boolean createNewRole(IdNameDO db);
	
}
