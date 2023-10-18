package com.ibm.srap.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.srap.client_beans.OperationResult;
import com.ibm.srap.client_beans.SubdomainDO;
import com.ibm.srap.models.Status;
import com.ibm.srap.models.Subdomain;
import com.ibm.srap.models.repositories.Domains;
import com.ibm.srap.models.repositories.Statuses;
import com.ibm.srap.models.repositories.Subdomains;
import com.ibm.srap.services.SubdomainService;
import com.ibm.srap.services.utils.Messages;

@Service("SubdomainService")
public class SubdomainServiceImpl implements SubdomainService {

	private static final Logger logger = Logger.getLogger(SubdomainServiceImpl.class.getName());

	@Autowired
	private Subdomains subdomains;
	@Autowired
	private Domains domains;
	@Autowired
	private Statuses statuses;

	@Override
	public List<SubdomainDO> getAllForDomain(Integer domainId) {
		List<Subdomain> domSubdomains = subdomains.findByDomainId(domainId);
		if (domSubdomains == null)
			return new ArrayList<>();

		List<SubdomainDO> response = new ArrayList<>();
		for (Subdomain sd : domSubdomains) {
			SubdomainDO bean = new SubdomainDO();
			bean.setId(sd.getId());
			bean.setName(sd.getName());
			bean.setDomainId(sd.getDomain().getId());
			bean.setSllName(sd.getSll());
			bean.setStatus(sd.getStatus().getName());
			response.add(bean);
		}
		return response;
	}

	@Override
	public OperationResult createNewSubdomain(SubdomainDO input) {
		if (!validateInputForCreate(input))
			return new OperationResult(false, Messages.BAD_REQUEST_INPUT);

		Subdomain entity = createNewEntity(input);

		if (entity == null || entity.getId() == null)
			return new OperationResult(false, Messages.ENTITY_NOT_SAVED);

		return new OperationResult(true, Messages.ENTITY_SAVED);
	}

	@Override
	public OperationResult updateSubdomain(SubdomainDO input) {
		if (!validateInputForUpdate(input))
			return new OperationResult(false, Messages.BAD_REQUEST_INPUT);

		Subdomain entity = subdomains.findOne(input.getId());

		if (entity == null)
			return new OperationResult(false, Messages.SUBDOMAIN_NOT_FOUND);

		entity = updateExistingEntity(entity, input);

		if (entity == null || entity.getId() == null)
			return new OperationResult(false, Messages.ENTITY_NOT_SAVED);

		return new OperationResult(true, Messages.ENTITY_SAVED);
	}

	private boolean validateInputForCreate(SubdomainDO input) {
		return (idIsProvided(input.getDomainId(), Messages.NO_DOMAIN_ID) && domainExists(input.getDomainId())
				&& nameIsProvided(input.getName()) && !subdomainExists(input.getName(), input.getDomainId()));
	}

	private boolean validateInputForUpdate(SubdomainDO input) {
		return (idIsProvided(input.getId(), Messages.NO_SUBDOMAIN_ID));
	}

	private boolean idIsProvided(Integer id, String logMessage) {
		if (id == null) {
			logger.log(Level.WARNING, logMessage);
			return false;
		}
		return true;
	}

	private boolean domainExists(Integer id) {
		if (!domains.exists(id)) {
			logger.log(Level.WARNING, Messages.DOMAIN_NOT_FOUND);
			return false;
		}
		return true;
	}

	private boolean nameIsProvided(String name) {
		if (name.isEmpty()) {
			logger.log(Level.WARNING, Messages.NO_NAME);
			return false;
		}
		return true;
	}

	private boolean subdomainExists(String name, Integer domainId) {
		if (subdomains.existsByNameAndDomainId(name, domainId)) {
			logger.log(Level.WARNING, Messages.ALREADY_EXISTS);
			return true;
		}
		return false;
	}

	private Subdomain createNewEntity(SubdomainDO input) {
		Subdomain entity = new Subdomain();
		entity.setName(input.getName());
		entity.setDomain(domains.findOne(input.getDomainId()));
		entity.setSll(input.getSllName());
		entity.setStatus(statuses.findOneByNameIgnoreCase("Active"));
		return subdomains.save(entity);
	}

	private Subdomain updateExistingEntity(Subdomain entity, SubdomainDO input) {
		boolean changed = false;
		if (!input.getName().isEmpty()) {
			entity.setName(input.getName());
			changed = true;
		}
		if ((input.getDomainId() != null) && (domains.exists(input.getDomainId()))) {
			entity.setDomain(domains.findOne(input.getDomainId()));
			changed = true;
		}

		if (!input.getSllName().isEmpty()) {
			entity.setSll(input.getSllName());
			changed = true;
		}

		if (!input.getStatus().isEmpty()) {
			Status st = statuses.findOneByNameIgnoreCase(input.getStatus());
			if (st == null)
				return entity;
			entity.setStatus(st);
			changed = true;
		}

		if (changed)
			entity = subdomains.save(entity);

		return entity;
	}

}
