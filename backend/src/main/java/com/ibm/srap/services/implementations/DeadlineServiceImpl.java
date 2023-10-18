package com.ibm.srap.services.implementations;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.srap.client_beans.DeadlineDO;
import com.ibm.srap.client_beans.OperationResult;
import com.ibm.srap.models.Deadline;
import com.ibm.srap.models.Domain;
import com.ibm.srap.models.repositories.Deadlines;
import com.ibm.srap.models.repositories.Domains;
import com.ibm.srap.services.DeadlineService;
import com.ibm.srap.services.utils.Messages;

@Service("DeadlineService")
public class DeadlineServiceImpl implements DeadlineService {

	@Autowired private Deadlines deadlines;
	@Autowired private Domains domains;
	
	
	@Override
	public List<DeadlineDO> getDeadlines(Integer input) {
		List<Deadline> deadlinesDB = deadlines.findAllByDomainId(input);
		if (deadlinesDB == null) return new ArrayList<>();
		
		List<DeadlineDO> response = new ArrayList<>();
		for (Deadline deadline : deadlinesDB)
				response.add(mapEntity(deadline));
		
		return response;
	}

	@Override
	public OperationResult createDeadline(DeadlineDO input) {
		if (input.getDomainId() == null) return new OperationResult(false, "No domain id provided");
		if (input.getQuarter() == null) return new OperationResult(false, "No quarter provided for deadline");
		if (input.getFllSignoffDeadline() == null) return new OperationResult(false, "No deadline provided for FLL sign-off");
		if (input.getFpSubmitDeadline() == null) return new OperationResult(false, "No deadline provided for FP submition");
		if (input.getSllRollupDeadline() == null) return new OperationResult(false, "No deadline provided for SLL roll-up");
		
		Deadline deadline = mapToEntity(input);
		deadline = deadlines.save(deadline);
		if (deadline.getId() == null) return new OperationResult(false, "Deadline save operation failed");
		
		return new OperationResult(true, "Deadline created");
	}

	@Override
	public OperationResult updateDeadline(DeadlineDO input) {
		if (input.getId() == null) return new OperationResult(false, "No deadline id provided");
		
		Deadline deadline = deadlines.findOne(input.getId());
		if (deadline == null) return new OperationResult(false, "No deadline found with this id");
		
		deadline = updateExistingEntity(deadline, input);
		
		if (deadline == null || deadline.getId() == null) 
			return new OperationResult(false, Messages.ENTITY_NOT_SAVED);
		
		return new OperationResult(true, "Deadline updated");
	}

	private DeadlineDO mapEntity(Deadline deadline) {
		DeadlineDO bean = new DeadlineDO();
		bean.setId(deadline.getId());
		bean.setDomainId(deadline.getDomain().getId());
		bean.setFllSignoffDeadline(deadline.getFllSignoffDeadline());
		bean.setFpSubmitDeadline(deadline.getFpSubmitDeadline());
		bean.setSllRollupDeadline(deadline.getSllRollupDeadline());
		bean.setQuarter(deadline.getQuarter());
		return bean;
	}
	
	private Deadline mapToEntity(DeadlineDO input) {
		Deadline entity = new Deadline();
		entity.setDomain(domains.findOne(input.getDomainId()));
		entity.setQuarter(input.getQuarter());
		entity.setFllSignoffDeadline(input.getFllSignoffDeadline());
		entity.setFpSubmitDeadline(input.getFpSubmitDeadline());
		entity.setSllRollupDeadline(input.getSllRollupDeadline());
		return entity;
	}
	
	private Deadline updateExistingEntity(Deadline entity, DeadlineDO input) {
		boolean changed = false;
		final Integer domainId = input.getDomainId();
		if (domainId != null) {
			Domain domain = domains.findOne(domainId);
			if (domain == null) return null;
			entity.setDomain(domain);
			changed = true;
		}
		
		final String quarter = input.getQuarter();
		if (!quarter.isEmpty()) { entity.setQuarter(quarter); changed = true; }
		
		final Timestamp fllTime = input.getFllSignoffDeadline();
		if (fllTime != null) { entity.setFllSignoffDeadline(fllTime); changed = true; }
		
		final Timestamp fpTime = input.getFpSubmitDeadline();
		if (fpTime != null) { entity.setFpSubmitDeadline(fpTime); changed = true; }
		
		final Timestamp sllTime = input.getSllRollupDeadline();
		if (sllTime != null) { entity.setSllRollupDeadline(sllTime); changed = true; }
		
		if (changed) entity = deadlines.save(entity);
		
		return entity;
	} 
}
