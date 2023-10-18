package com.ibm.srap.services.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.srap.client_beans.OperationResult;
import com.ibm.srap.client_beans.ThresholdDO;
import com.ibm.srap.models.Domain;
import com.ibm.srap.models.Threshold;
import com.ibm.srap.models.repositories.Domains;
import com.ibm.srap.models.repositories.Thresholds;
import com.ibm.srap.services.ThresholdService;
import com.ibm.srap.services.utils.RatingValues;
import com.ibm.srap.services.utils.SrapUtils;

@Service("ThresholdService")
public class ThresholdServiceImpl implements ThresholdService {

	@Autowired
	Thresholds thresholds;
	@Autowired
	Domains domains;

	@Override
	public OperationResult createNewThreshold(ThresholdDO thresholdDO) {
		if ((thresholdDO.getDomainId() == null) && (thresholdDO.getMarginalThreshold() == null)
				&& (thresholdDO.getSatThreshold() == null)) {
			return new OperationResult(false, "Unable to create a new threshold, check for null or empty fields.");
		}

		if ((thresholds.countByDomainIdAndQuarterAndYear(thresholdDO.getDomainId(), SrapUtils.currentQuarter(),
				SrapUtils.currentYear()) > 0)) {
			return new OperationResult(false, "This threshold already exists for current quarter, only update is allowed.");
		}
		Threshold thresholdEntity = new ModelMapper().map(thresholdDO, Threshold.class);

		Domain domain = domains.findOne(thresholdDO.getDomainId());
		if (domain != null) {
			thresholdEntity.setDomain(domain);
		} else {
			return new OperationResult(false, "The domain ID entered does not exist.");
		}

		thresholdEntity.setMarginalThreshold(thresholdDO.getMarginalThreshold());
		thresholdEntity.setSatThreshold(thresholdDO.getSatThreshold());
		thresholdEntity.setQuarter(SrapUtils.currentQuarter());
		thresholdEntity.setYear(SrapUtils.currentYear());

		thresholdEntity = thresholds.save(thresholdEntity);

		if (thresholdEntity.getId() != null) {
			return new OperationResult(true, "New threshold for current quarter was created.");
		} else {
			return new OperationResult(false, "Unable to create a new threshold, please try again.");
		}
	}

	@Override
	public ThresholdDO getThresholdsByDomainIdForCurrentQuarterAndYear(Integer domainId) {
		List<Threshold> thresholdList = thresholds.findAllByDomainId(domainId);

		if (thresholdList.isEmpty()) {

			ThresholdDO thresholdDO = new ThresholdDO();

			thresholdDO.setMarginalThreshold(RatingValues.MARGINAL_THRESHOLD);
			thresholdDO.setSatThreshold(RatingValues.SAT_THRESHOLD);
			thresholdDO.setQuarter("");
			thresholdDO.setYear("");

			return thresholdDO;

		} else {

			Collections.sort(thresholdList, new Comparator<Threshold>() {

				@Override
				public int compare(Threshold o1, Threshold o2) {
					return o2.getYear().concat(o2.getQuarter()).compareTo(o1.getYear().concat(o1.getQuarter()));
				}

			});

			Threshold thresholdEntity = thresholdList.get(0);

			ThresholdDO thresholdDO = new ThresholdDO();
			thresholdDO.setDomainId(thresholdEntity.getDomain().getId());
			thresholdDO.setId(thresholdEntity.getId());
			thresholdDO.setMarginalThreshold(thresholdEntity.getMarginalThreshold());
			thresholdDO.setSatThreshold(thresholdEntity.getSatThreshold());
			thresholdDO.setQuarter(thresholdEntity.getQuarter());
			thresholdDO.setYear(thresholdEntity.getYear());

			return thresholdDO;
		}

	}

	@Override
	public OperationResult updateThreshold(ThresholdDO thresholdDO) {
		if (thresholdDO.getId() == null)
			return new OperationResult(false, "No threshold id provided.");

		Threshold threshold = thresholds.findByIdAndQuarterAndYear(thresholdDO.getId(), SrapUtils.currentQuarter(),
				SrapUtils.currentYear());
		if (threshold == null)
			return new OperationResult(false, "No threshold found for current quarter-year.");

		threshold.setMarginalThreshold(thresholdDO.getMarginalThreshold());
		threshold.setSatThreshold(thresholdDO.getSatThreshold());

		thresholds.save(threshold);
		return new OperationResult(true, "Threshold updated");
	}

	@Override
	public ThresholdDO getSpecificThresholdByDomainIdQuarterYear(Integer domainId, String quarter, String year) {

		List<Threshold> thresholdsList = thresholds.findSpecificByDomainIdAndQuarterAndYear(domainId, quarter, year);

		if (thresholdsList.isEmpty()) {

			ThresholdDO thresholdDO = new ThresholdDO();

			thresholdDO.setId(0);
			thresholdDO.setMarginalThreshold(RatingValues.MARGINAL_THRESHOLD);
			thresholdDO.setSatThreshold(RatingValues.SAT_THRESHOLD);
			thresholdDO.setQuarter("");
			thresholdDO.setYear("");

			return thresholdDO;
		} else {

			Threshold thresholdEntity = thresholdsList.get(0);

			ThresholdDO thresholdDO = new ThresholdDO();
			thresholdDO.setDomainId(thresholdEntity.getDomain().getId());
			thresholdDO.setId(thresholdEntity.getId());
			thresholdDO.setMarginalThreshold(thresholdEntity.getMarginalThreshold());
			thresholdDO.setSatThreshold(thresholdEntity.getSatThreshold());
			thresholdDO.setQuarter(thresholdEntity.getQuarter());
			thresholdDO.setYear(thresholdEntity.getYear());

			return thresholdDO;
		}

	}

	@Override
	public List<ThresholdDO> getAllThresholdsByDomainId(Integer domainId) {

		List<Threshold> thresholdsList = thresholds.findAllByDomainId(domainId);

		List<ThresholdDO> thresholdDOs = new ArrayList<>();

		for (Threshold thresholdEntity : thresholdsList) {

			ThresholdDO thresholdDO = new ThresholdDO();
			thresholdDO.setId(thresholdEntity.getId());
			thresholdDO.setDomainId(thresholdEntity.getDomain().getId());
			thresholdDO.setMarginalThreshold(thresholdEntity.getMarginalThreshold());
			thresholdDO.setSatThreshold(thresholdEntity.getSatThreshold());
			thresholdDO.setQuarter(thresholdEntity.getQuarter());
			thresholdDO.setYear(thresholdEntity.getYear());

			thresholdDOs.add(thresholdDO);

		}

		return thresholdDOs;
	}

}
