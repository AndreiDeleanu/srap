package com.ibm.srap.services.implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.srap.client_beans.OperationResult;
import com.ibm.srap.client_beans.SquadDO;
import com.ibm.srap.client_beans.SquadUpdateDO;
import com.ibm.srap.client_beans.SubdomainDO;
import com.ibm.srap.models.Squad;
import com.ibm.srap.models.Status;
import com.ibm.srap.models.Subdomain;
import com.ibm.srap.models.repositories.Ratings;
import com.ibm.srap.models.repositories.Squads;
import com.ibm.srap.models.repositories.Statuses;
import com.ibm.srap.models.repositories.Subdomains;
import com.ibm.srap.services.SquadService;

@Service("SquadService")
public class SquadServiceImpl implements SquadService {

	@Autowired private Squads squads;
	@Autowired private Ratings ratings;
	@Autowired private Subdomains subdomains;
	@Autowired private Statuses statuses;
	
	
	@Override
	public List<SquadDO> getSquadsByDomain(Integer domainId) {
		List<Squad> domainSquads = squads.findByDomainId(domainId);
		List<SquadDO> result = new ArrayList<>();
		if (domainSquads == null) return result;
		
		for (Squad entity : domainSquads) {
			result.add(mapEntityToBean(entity));
		}
		
		return result;
	}

	@Override
	public boolean createNewSquad(SquadDO squadBean) {
		if (squadBean == null) return false;
		
		Squad entity = new Squad();
		entity.setFll(squadBean.getFll().toLowerCase());
		entity.setFp(squadBean.getFocalPoint().toLowerCase());
		SubdomainDO subdomainBean = squadBean.getSubdomain();
        if (subdomainBean != null) {
        	entity.setSubdomain(subdomains.findOne(subdomainBean.getId()));
        }
		entity.setName(squadBean.getName());
		entity.setRating(squadBean.getRating());
		entity.setStatus(statuses.findOneByNameIgnoreCase("Active"));
		entity = squads.save(entity);
		
		return (entity.getId() != null);
	}
	
	@Override
	public OperationResult updateSquad(SquadUpdateDO input) {
		if (input.getId() == null) return new OperationResult(false, "No squad id provided");
		boolean changed = false;
		Squad squad = squads.findOne(input.getId());
		if (squad == null) return new OperationResult(false, "There is no squad with this id in the database");
		
		if (!input.getFll().isEmpty()) {
			squad.setFll(input.getFll());
			changed = true;
		}
		if (!input.getFp().isEmpty()) {
			squad.setFp(input.getFp());
			changed = true;
		}
		if (!input.getName().isEmpty()) {
			squad.setName(input.getName());
			changed = true;
		}
		if (input.getSubdomainId() != null) {
			Subdomain subdomain = subdomains.findOne(input.getSubdomainId());
			if (subdomain == null) return new OperationResult(false, "There is no subdomain with this id in the database");
			squad.setSubdomain(subdomain);
			changed = true;
		}
		
		if (!input.getStatus().isEmpty()) {
			Status status = statuses.findOneByNameIgnoreCase(input.getStatus());
			if (status == null) return new OperationResult(false, "There is no status with this id in the database");
			squad.setStatus(status);
			changed = true;
		}
		
		if (changed) {
			squads.save(squad);
			return new OperationResult(true, "Squad updated");
		} else {
			return new OperationResult(false, "No changes were made, missing input");
		}
	}
	
	@Override
	public SquadDO getSquadForUser(String user) {
		SquadDO bean = new SquadDO();
		List<Squad> userSquads = squads.findSquadFor(user);
		if (userSquads != null && !userSquads.isEmpty()) {
			Squad s = userSquads.get(0);
			bean.setId(s.getId());
			bean.setName(s.getName());
			bean.setRating(s.getRating());
			bean.setFll(s.getFll());
			bean.setFocalPoint(s.getFp());
			Subdomain subdomain = s.getSubdomain();
			if (subdomain != null) {
				SubdomainDO subdomainBean = new SubdomainDO();
				subdomainBean.setId(subdomain.getId());
				subdomainBean.setName(subdomain.getName());
				subdomainBean.setSllName(subdomain.getSll());
				subdomainBean.setDomainId(subdomain.getDomain().getId());
				bean.setDomainOwner(subdomain.getDomain().getOwner());
				bean.setSubdomain(subdomainBean);
			}
		}
		return bean;
	}
	
	private SquadDO mapEntityToBean(Squad entity) {
		SquadDO bean = new SquadDO();
		bean.setFll(entity.getFll());
		bean.setFocalPoint(entity.getFp());
		bean.setId(entity.getId());
		bean.setName(entity.getName());
		bean.setStatus(entity.getStatus().getName());
		if (entity.getRating() != null) bean.setRating(ratings.findOne(entity.getRating()).getValue());
		Subdomain subdomain = entity.getSubdomain();
		if (subdomain != null) {
			SubdomainDO subdomainBean = new SubdomainDO();
			subdomainBean.setId(subdomain.getId());
			subdomainBean.setName(subdomain.getName());
			subdomainBean.setSllName(subdomain.getSll());
			subdomainBean.setDomainId(subdomain.getDomain().getId());
			subdomainBean.setStatus(subdomain.getStatus().getName());
			bean.setDomainOwner(subdomain.getDomain().getOwner());
			bean.setSubdomain(subdomainBean);
		}
		return bean;
	}

}
