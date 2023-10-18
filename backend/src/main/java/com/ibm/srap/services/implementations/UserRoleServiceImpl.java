package com.ibm.srap.services.implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.srap.client_beans.RoleMappingDO;
import com.ibm.srap.models.Delegation;
import com.ibm.srap.models.Role;
import com.ibm.srap.models.UserRole;
import com.ibm.srap.models.repositories.Delegations;
import com.ibm.srap.models.repositories.Domains;
import com.ibm.srap.models.repositories.Roles;
import com.ibm.srap.models.repositories.Squads;
import com.ibm.srap.models.repositories.Subdomains;
import com.ibm.srap.models.repositories.UserRoles;
import com.ibm.srap.services.UserRoleService;
import com.ibm.srap.services.utils.RoleNames;

@Service("UserRoleService")
public class UserRoleServiceImpl implements UserRoleService {
	
	@Autowired private Roles roles;
	@Autowired private UserRoles userRoles;
	@Autowired private Domains domains;
	@Autowired private Squads squads;
	@Autowired private Subdomains subdomains;
	@Autowired private Delegations delegations;
	
	private static final String ACTIVE = "Active";
	
	
	@Override
	public List<Integer> getDomainForUser(String user) {
		return userRoles.findDomainIdByUser(user);
	}

	@Override
	public List<String> lookupRoles(String user) {
		validateRoleMappings(squads.getDomainByFll(user), roles.findByName(RoleNames.FLL.name()), user);
		validateRoleMappings(subdomains.getDomainBySll(user), roles.findByName(RoleNames.SLL.name()), user);
		validateRoleMappings(squads.getDomainByFp(user), roles.findByName(RoleNames.FP.name()), user);
		
		List<Integer> domainsByOwner = domains.getDomainsByOwner(user);
		if (domainsByOwner != null) {
			for (Integer domainWithOwnerReference : domainsByOwner) {
				validateRoleMappings(domainWithOwnerReference, roles.findByName(RoleNames.DOMAIN_OWNER.name()), user);
			}
		}
		
		List<Integer> domainsByAdmin = domains.getDomainsByAdmin(user);
		if (domainsByAdmin != null) {
			for (Integer domainWithAdminReference : domainsByAdmin) {
				validateRoleMappings(domainWithAdminReference, roles.findByName(RoleNames.DOMAIN_ADMIN.name()), user);
			}
		}
		
		List<String> currentRoles = userRoles.getRolesByUser(user);
		
		for (String role : currentRoles) {
			checkAndRemoveIfRoleObsolete(role, user);
		}
		
		return userRoles.getRolesByUser(user);
	}
	
	private void checkAndRemoveIfRoleObsolete(String role, String user) {
		if (
			(RoleNames.FLL.name().equalsIgnoreCase(role) && !squads.existsByFllAndStatusName(user, ACTIVE)) ||
			(RoleNames.FP.name().equalsIgnoreCase(role) && !squads.existsByFpAndStatusName(user, ACTIVE)) ||
			(RoleNames.SLL.name().equalsIgnoreCase(role) && !subdomains.existsBySllAndStatusName(user, ACTIVE)) ||
			(RoleNames.DOMAIN_ADMIN.name().equalsIgnoreCase(role) && !domains.existsByAdminAndStatusName(user, ACTIVE)) ||
			(RoleNames.DOMAIN_OWNER.name().equalsIgnoreCase(role) && !domains.existsByOwnerAndStatusName(user, ACTIVE))
			) {
			UserRole roleToRemove = userRoles.findOneByUserMailAndRoleName(user, role);
			
			List<Delegation> delegationsToRemove = delegations.findAllByRoleId(roleToRemove.getId());
			if (delegationsToRemove != null && !delegationsToRemove.isEmpty()) delegations.delete(delegationsToRemove);
			
			userRoles.delete(roleToRemove);
		}
	}

	@Override
	public List<RoleMappingDO> getRoleMappings(String user) {
		List<RoleMappingDO> mappings = new ArrayList<>();
		List<Integer> domainIds = userRoles.findDomainIdByUser(user);
		if (domainIds == null) return mappings;
		for (Integer domainId : domainIds) {
			RoleMappingDO map = new RoleMappingDO(domainId, userRoles.getRolesByUserAndDomain(user, domainId));
			mappings.add(map);
		}
		return mappings;
	}
	
	private void validateRoleMappings(Integer domain, Role role, String user) {
		if (domain == null) return;
		if (userRoles.existsByDomainIdAndRoleIdAndUserMail(domain, role.getId(), user)) return;
		
		UserRole roleMapping = new UserRole();
		roleMapping.setDomain(domains.findOne(domain));
		roleMapping.setRole(role);
		roleMapping.setUserMail(user);
		userRoles.save(roleMapping);
	}


}
