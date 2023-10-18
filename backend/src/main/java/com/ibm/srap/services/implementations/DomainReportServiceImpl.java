package com.ibm.srap.services.implementations;

import static com.ibm.srap.services.utils.SrapUtils.currentQuarter;
import static com.ibm.srap.services.utils.SrapUtils.currentYear;
import static com.ibm.srap.services.utils.SrapUtils.hasData;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.srap.client_beans.DomainReportDO;
import com.ibm.srap.client_beans.OperationResult;
import com.ibm.srap.client_beans.ProcessAssessmentDO;
import com.ibm.srap.models.Domain;
import com.ibm.srap.models.DomainProcessAssessment;
import com.ibm.srap.models.DomainReport;
import com.ibm.srap.models.Rating;
import com.ibm.srap.models.Squad;
import com.ibm.srap.models.repositories.DomainProcessAssessments;
import com.ibm.srap.models.repositories.DomainReports;
import com.ibm.srap.models.repositories.Domains;
import com.ibm.srap.models.repositories.ProcessAssessments;
import com.ibm.srap.models.repositories.Processes;
import com.ibm.srap.models.repositories.Ratings;
import com.ibm.srap.models.repositories.SquadReports;
import com.ibm.srap.models.repositories.Squads;
import com.ibm.srap.services.DomainReportService;
import com.ibm.srap.services.ThresholdService;
import com.ibm.srap.services.utils.Messages;
import com.ibm.srap.services.utils.RatingValues;

@Service("DomainReportService")
public class DomainReportServiceImpl implements DomainReportService {

	@Autowired
	private Domains domains;
	@Autowired
	private DomainReports domainReports;
	@Autowired
	private DomainProcessAssessments domainProcessAssessments;
	@Autowired
	private Ratings ratings;
	@Autowired
	private SquadReports squadReports;
	@Autowired
	private Processes processes;
	@Autowired
	private ProcessAssessments processAssessments;
	@Autowired
	private HttpSession httpSession;
	@Autowired
	private Squads squads;
	@Autowired
	private ThresholdService thresholdService;

	@Override
	public OperationResult saveReport(DomainReportDO input) {
		if (input.getId() == null)
			return new OperationResult(false, Messages.NO_DOMAIN_ID);

		Domain domain = domains.findOne(input.getId());
		if (domain == null)
			return new OperationResult(false, Messages.DOMAIN_NOT_FOUND);

		DomainReport report = domainReports.findOneByDomainIdAndQuarterAndYear(input.getId(), currentQuarter(),
				currentYear());
		if (report == null)
			report = createNewEntity();

		if (report.getDomain() == null)
			report.setDomain(domain);

		if (hasData(input.getDoComment()))
			report.setDoComment(input.getDoComment());
		if (hasData(input.getDoAssessment()))
			report.setDoRating(ratings.findOneByName(input.getDoAssessment()));
		if (hasData(input.getProjection()))
			report.setProjection(ratings.findOneByName(input.getProjection()));
		if (hasData(input.getQuarter()))
			report.setQuarter(input.getQuarter());
		if (hasData(input.getYear()))
			report.setYear(input.getYear());

		List<Squad> squadList = squads.findByDomainId(domain.getId());
		boolean existsSquad = false;

		if (!squadList.isEmpty()) {
			calculateFllAverages(input, report);
			existsSquad = true;
		} else {
			report.setFllAverage(null);
			report.setFllRating(null);
		}

		report.setUpdatedBy((String) httpSession.getAttribute(Messages.LOGGED_IN_USER));
		report = domainReports.save(report);

		saveProcessAssessments(input.getProcessAssessments(), report, existsSquad);

		return new OperationResult(true, Messages.ENTITY_SAVED);
	}

	private DomainReport createNewEntity() {
		DomainReport report = new DomainReport();
		report.setQuarter(currentQuarter());
		report.setYear(currentYear());
		report.setCreatedBy((String) httpSession.getAttribute(Messages.LOGGED_IN_USER));
		return report;
	}

	private void saveProcessAssessments(List<ProcessAssessmentDO> assessments, DomainReport report, boolean existsSquad) {
		for (ProcessAssessmentDO bean : assessments) {
			final Integer processID = bean.getId();
			final Integer domainID = report.getDomain().getId();

			DomainProcessAssessment dpa = domainProcessAssessments.findOneByProcessIdAndReportId(processID, report.getId());
			if (dpa == null)
				dpa = new DomainProcessAssessment();

			dpa.setProcess(processes.findOne(processID));
			dpa.setComment(bean.getDaComment());
			dpa.setReport(report);
			if (existsSquad) {
				dpa.setAverage(calculateProcessRatingsAverage(processID, domainID));
			} else {
				dpa.setAverage(null);
			}

			domainProcessAssessments.save(dpa);
		}
	}

	private void calculateFllAverages(DomainReportDO input, DomainReport report) {
		List<Integer> fllAverages = squadReports.findAllCurrentFLLRatingsByDomain(input.getId(), currentQuarter(),
				currentYear());

		RatingValuesMap ratingsMap = gatherRatingValues(fllAverages);

		if (ratingsMap.getNumberOfRatings() > 0) {
			report.setFllAverage(
					RatingValues.calculatePercentage(ratingsMap.getValuesTotal(), ratingsMap.getNumberOfRatings()));
			report.setFllRating(ratings
					.findOneByValue(RatingValues.averageValues(ratingsMap.getValuesTotal(), ratingsMap.getNumberOfRatings(),
							thresholdService.getThresholdsByDomainIdForCurrentQuarterAndYear(input.getId()))));
		}
	}

	private Rating calculateProcessRatingsAverage(Integer processID, Integer domainID) {
		List<Integer> processRatingValues = processAssessments.findAllCurrentProcessAssessments(processID, domainID,
				currentQuarter(), currentYear());

		int total = 0;
		int numberOfValues = 0;
		for (Integer rating : processRatingValues) {
			if (rating != null && rating != RatingValues.DB_NA) {
				if (rating == RatingValues.DB_SAT)
					total += RatingValues.ADJUSTED_SAT;
				if (rating == RatingValues.DB_MARGINAL)
					total += RatingValues.ADJUSTED_MARGINAL;
				if (rating == RatingValues.DB_UNSAT)
					total += RatingValues.ADJUSTED_UNSAT;
				numberOfValues++;
			}
		}

		if (numberOfValues <= 0) {
			for (Integer value : processRatingValues) {
				if (value == RatingValues.DB_NA) {
					return ratings.findOneByValue(RatingValues.DB_NA);
				}
			}
		} else {
			return ratings.findOneByValue(RatingValues.averageValues(total, numberOfValues,
					thresholdService.getThresholdsByDomainIdForCurrentQuarterAndYear(domainID)));
		}
		return null;

		/*
		 * RatingValuesMap ratingsMap = gatherRatingValues(processRatingValues);
		 * 
		 * if (ratingsMap.getNumberOfRatings() <= 0) { return
		 * ratings.findOneByValue(RatingValues.DB_UNSAT); }
		 */

	}

	private RatingValuesMap gatherRatingValues(List<Integer> ratings) {
		int total = 0;
		int numberOfValues = 0;
		for (Integer rating : ratings) {
			if (rating == null || rating == RatingValues.DB_NA)
				continue;
			if (rating == RatingValues.DB_SAT)
				total += RatingValues.ADJUSTED_SAT;
			if (rating == RatingValues.DB_MARGINAL)
				total += RatingValues.ADJUSTED_MARGINAL;
			if (rating == RatingValues.DB_UNSAT)
				total += RatingValues.ADJUSTED_UNSAT;
			numberOfValues++;
		}
		return new RatingValuesMap(total, numberOfValues);
	}

	@Override
	public List<DomainReportDO> getDomainReports(Integer domainId, String quarterInput) {
		String quarter = quarterInput == null ? currentQuarter() : quarterInput.split("Q")[0];
		String year = quarterInput == null ? currentYear() : quarterInput.split("Q")[1];
		List<DomainReportDO> response = new ArrayList<>();
		DomainReport report = domainReports.findOneByDomainIdAndQuarterAndYear(domainId, quarter, year);
		if (report == null)
			return response;
		response.add(mapEntity(report));
		return response;
	}

	private DomainReportDO mapEntity(DomainReport report) {
		DomainReportDO bean = new DomainReportDO();
		bean.setDomainReportId(report.getId());
		bean.setId(report.getDomain().getId());
		if (report.getDomain() != null)
			bean.setName(report.getDomain().getName());
		if (report.getDoRating() != null)
			bean.setDoAssessment(report.getDoRating().getName());
		if (report.getFllRating() != null)
			bean.setFllRating(report.getFllRating().getName());
		if (report.getProjection() != null)
			bean.setProjection(report.getProjection().getName());
		bean.setDoComment(report.getDoComment());
		bean.setFllAverage(report.getFllAverage());
		bean.setQuarter(report.getQuarter());
		bean.setYear(report.getYear());

		List<DomainProcessAssessment> dpas = domainProcessAssessments.findAllByReportId(report.getId());
		for (DomainProcessAssessment dpa : dpas) {
			ProcessAssessmentDO processBean = new ProcessAssessmentDO();
			processBean.setDaComment(dpa.getComment());
			if (dpa.getAverage() != null)
				processBean.setAverage(dpa.getAverage().getName());
			processBean.setId(dpa.getProcess().getId());
			processBean.setName(dpa.getProcess().getName());

			bean.getProcessAssessments().add(processBean);
		}

		bean.getHistory().setCreatedBy(report.getCreatedBy());
		bean.getHistory().setCreatedTime(report.getCreatedTime());
		bean.getHistory().setLastModifiedBy(report.getUpdatedBy());
		bean.getHistory().setLastModifiedTime(report.getUpdatedTime());

		return bean;
	}

	private class RatingValuesMap {
		private int valuesTotal;
		private int numberOfRatings;

		public RatingValuesMap(int totalToSet, int values) {
			valuesTotal = totalToSet;
			numberOfRatings = values;
		}

		public int getValuesTotal() {
			return valuesTotal;
		}

		public int getNumberOfRatings() {
			return numberOfRatings;
		}
	}
}
