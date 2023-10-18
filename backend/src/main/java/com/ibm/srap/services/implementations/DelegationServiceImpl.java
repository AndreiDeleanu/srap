package com.ibm.srap.services.implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.srap.client_beans.DelegationDO;
import com.ibm.srap.client_beans.DelegationInfoDO;
import com.ibm.srap.client_beans.OperationResult;
import com.ibm.srap.models.Delegation;
import com.ibm.srap.models.UserRole;
import com.ibm.srap.models.repositories.Delegations;
import com.ibm.srap.models.repositories.UserRoles;
import com.ibm.srap.services.DelegationService;

@Service("DelegationService")
public class DelegationServiceImpl implements DelegationService {

	@Autowired private Delegations delegations;
	@Autowired private UserRoles userRoles;
	
	
	@Override
	public List<DelegationDO> getDelegations(String input) {
		List<DelegationDO> response = new ArrayList<>();
		if (input.isEmpty()) return response;
		
		List<Delegation> userDelegations = delegations.findAllByDelegateOrDelegator(input, input);
		if (userDelegations == null) return response;
		
		for (Delegation delegation : userDelegations) response.add(mapToBean(delegation));
		
		return response;
	}
	
	@Override
	public OperationResult createDelegation(DelegationDO input) {
		if (input.getDelegator().isEmpty()) return new OperationResult(false, "No delegator provided");
		if (input.getDelegate().isEmpty()) return new OperationResult(false, "No delegate provided");
		if (input.getRole().isEmpty()) return new OperationResult(false, "No role provided");
		UserRole role = userRoles.findOneByUserMailAndRoleName(input.getDelegator(), input.getRole());
		if (role == null) return new OperationResult(false, "Delegator hasn't the specified role");
		
		Delegation delegation = mapToEntity(input, role);
		delegation = delegations.save(delegation);
		if (delegation.getId() == null) return new OperationResult(false, "Delegation save operation failed");
		
		return new OperationResult(true, "Delegation created");
	}

	@Override
	public OperationResult updateDelegation(DelegationDO input) {
		if (input.getId() == null) return new OperationResult(false, "No delegation id provided");
		
		Delegation delegation = delegations.findOne(input.getId());
		if (delegation == null) return new OperationResult(false, "No delegation found with this id");
		
		if (!input.getDelegator().isEmpty()) delegation.setDelegator(input.getDelegator().toLowerCase());
		if (!input.getDelegate().isEmpty()) delegation.setDelegate(input.getDelegate().toLowerCase());
		if (!input.getRole().isEmpty()) {
			UserRole role = userRoles.findOneByUserMailAndRoleName(delegation.getDelegator(), input.getRole());
			if (role == null) return new OperationResult(false, "Delegator hasn't the specified role");
			delegation.setUserRole(role);
		}
		
		delegations.save(delegation);
		return new OperationResult(true, "Delegation updated");
	}

	@Override
	public OperationResult deleteDelegation(DelegationDO input) {
		if (input.getId() == null) return new OperationResult(false, "No delegation id provided");
		
		Delegation delegation = delegations.findOne(input.getId());
		if (delegation == null) return new OperationResult(false, "No delegation found with this id");
		
		delegations.delete(delegation);
		
		return new OperationResult(true, "Delegation removed");
	}
	
	@Override
	public List<DelegationInfoDO> getForDelegate(String input) {
		List<DelegationInfoDO> response = new ArrayList<>();
		List<Delegation> userDelegations = delegations.findAllByDelegate(input);
		if (userDelegations == null) return response;
		for (Delegation delegation : userDelegations) response.add(mapToInfoBean(delegation));
		return response;
	}
	
	private DelegationDO mapToBean(Delegation delegation) {
		DelegationDO bean = new DelegationDO();
		bean.setDelegator(delegation.getDelegator());
		bean.setDelegate(delegation.getDelegate());
		bean.setId(delegation.getId());
		bean.setRole(delegation.getUserRole().getRole().getName());
		return bean;
	}
	
	private Delegation mapToEntity(DelegationDO input, UserRole role) {
		Delegation delegation = new Delegation();
		delegation.setDelegator(input.getDelegator().toLowerCase());
		delegation.setDelegate(input.getDelegate().toLowerCase());
		delegation.setUserRole(role);
		return delegation;
	}
	
	private DelegationInfoDO mapToInfoBean(Delegation delegation) {
		DelegationInfoDO bean = new DelegationInfoDO();
		bean.setDomainId(delegation.getUserRole().getDomain().getId());
		bean.setDelegatorEmail(delegation.getDelegator());
		bean.setRole(delegation.getUserRole().getRole().getName());
		return bean;
	}

}
