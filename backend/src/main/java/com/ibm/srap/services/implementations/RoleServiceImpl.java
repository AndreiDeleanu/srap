package com.ibm.srap.services.implementations;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.srap.client_beans.IdNameDO;
import com.ibm.srap.models.Role;
import com.ibm.srap.models.repositories.Roles;
import com.ibm.srap.services.RoleService;

@Service("RoleService")
public class RoleServiceImpl implements RoleService {
	
	@Autowired private Roles roles;

	
	@Override
	public List<IdNameDO> getAllRoles() {
		return getRoleBeans("");
	}

	@Override
	public List<IdNameDO> getRolesByName(String name) {
		return getRoleBeans(name);
	}
	
	@Override
	public boolean createNewRole(IdNameDO bean) {
		if (bean == null) return false;
		
		Role entity = new ModelMapper().map(bean, Role.class);
		entity = roles.save(entity);
		
		return (entity.getId() != null);
	}
	
	private List<IdNameDO> getRoleBeans(String name) {
		List<Role> entities = new ArrayList<>();
		if (name.isEmpty()) {
			entities.addAll(roles.findAll());
		} else {
			entities.add(roles.findByName(name));
		}
		
		List<IdNameDO> roleBeans = new ArrayList<>();
		for (Role entity : entities) {
			IdNameDO bean = new ModelMapper().map(entity, IdNameDO.class);
			roleBeans.add(bean);
		}
		return roleBeans;
	}

}
