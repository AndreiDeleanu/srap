package com.ibm.srap.services.implementations;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.srap.client_beans.DomainDO;
import com.ibm.srap.client_beans.OperationResult;
import com.ibm.srap.models.Domain;
import com.ibm.srap.models.Status;
import com.ibm.srap.models.repositories.Domains;
import com.ibm.srap.models.repositories.Statuses;
import com.ibm.srap.services.DomainService;

@Service("DomainService")
public class DomainServiceImpl implements DomainService {

	@Autowired private Domains domains;
	@Autowired private Statuses statuses;
	
	
	@Override
	public List<DomainDO> getAllDomains() {
		List<Domain> entities = new ArrayList<>();
		List<DomainDO> domainBeans = new ArrayList<>();
		entities.addAll(domains.findAll());
		if (entities.isEmpty()) return domainBeans;
		
		for (Domain entity : entities) {
			domainBeans.add(new ModelMapper().map(entity, DomainDO.class));
		}
		return domainBeans;
	}
	
	@Override
	public DomainDO getDomainBy(Integer id) {
		Domain domain = domains.findOne(id);
		if (domain == null) return new DomainDO();
		
		return new ModelMapper().map(domain, DomainDO.class);
	}

	@Override
	public OperationResult createNewDomain(DomainDO domainBean) {
		if (domainBean.getName().isEmpty()) 
			return new OperationResult(false, "No domain name provided");
		if (domains.existsByNameIgnoreCase(domainBean.getName()))
			return new OperationResult(false, "This domain already exists");
		if (domainBean.getOwner().isEmpty())
			return new OperationResult(false, "No domain owner provided");
		
		Domain entity = new ModelMapper().map(domainBean, Domain.class);
		Status status = statuses.findOneByNameIgnoreCase(domainBean.getStatus());
		if (status == null) {
			entity.setStatus(statuses.findOneByNameIgnoreCase("Inactive"));
		} else {
			entity.setStatus(status);
		}
		
		entity = domains.save(entity);
		if (entity.getId() == null) {
			return new OperationResult(false, "Problems encountered, domain was not saved");
		} else {
			return new OperationResult(true, "Domain '" + domainBean.getName() + "' created");
		}
	}

	@Override
	public OperationResult updateDomain(DomainDO bean) {
		if (bean.getId() == null) return new OperationResult(false, "No domain id provided");
		
		Domain domain = domains.findOne(bean.getId());
		if (domain == null) return new OperationResult(false, "No domain found with this id");
		
		if (!bean.getName().isEmpty()) domain.setName(bean.getName());
		if (!bean.getOwner().isEmpty()) domain.setOwner(bean.getOwner());
		if (!bean.getAdmin().isEmpty()) domain.setAdmin(bean.getAdmin());
		if (!bean.getHelp().isEmpty()) domain.setHelp(bean.getHelp());
		if (!bean.getStatus().isEmpty()) {
			Status status = statuses.findOneByNameIgnoreCase(bean.getStatus());
			if (status == null) return new OperationResult(false, "No status found with this name");
			domain.setStatus(status);
		}
		
		domains.save(domain);
		return new OperationResult(true, "Domain updated");
	}

}
