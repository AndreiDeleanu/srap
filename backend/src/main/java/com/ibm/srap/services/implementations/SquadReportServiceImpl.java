package com.ibm.srap.services.implementations;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ibm.srap.client_beans.CalculatedValuesGraphDO;
import com.ibm.srap.client_beans.EditSquadReportDO;
import com.ibm.srap.client_beans.FLLRatingGraphDO;
import com.ibm.srap.client_beans.ProcessRatingDO;
import com.ibm.srap.client_beans.ProgressGraphDO;
import com.ibm.srap.client_beans.ReportStatusDO;
import com.ibm.srap.client_beans.SquadReportDO;
import com.ibm.srap.models.ProcessAssessment;
import com.ibm.srap.models.Squad;
import com.ibm.srap.models.Process;
import com.ibm.srap.models.SquadReport;
import com.ibm.srap.models.Status;
import com.ibm.srap.models.repositories.ProcessAssessments;
import com.ibm.srap.models.repositories.Processes;
import com.ibm.srap.models.repositories.Ratings;
import com.ibm.srap.models.repositories.SquadReports;
import com.ibm.srap.models.repositories.Squads;
import com.ibm.srap.models.repositories.Statuses;
import com.ibm.srap.services.SquadReportService;
import com.ibm.srap.services.utils.Messages;
import com.ibm.srap.services.utils.RatingValues;
import com.ibm.srap.services.utils.SquadReportSteps;
import com.ibm.srap.services.utils.SrapUtils;

@Service("SquadReportService")
public class SquadReportServiceImpl implements SquadReportService {

	@Autowired
	private SquadReports squadReports;
	@Autowired
	private Statuses statuses;
	@Autowired
	private ProcessAssessments processAssessments;
	@Autowired
	private Squads squads;
	@Autowired
	private Ratings ratings;
	@Autowired
	private Processes processes;
	@Autowired
	private HttpSession httpSession;
	@Autowired
	EntityManager entityManger;
	@Autowired
	Environment env;

	@Override
	public EditSquadReportDO getReport(int reportId) {
		SquadReport report = squadReports.findOne(reportId);
		if (report == null)
			return new EditSquadReportDO();

		return mapReportToClientBean(report);
	}

	@Override
	public EditSquadReportDO getCurrentQuarterSquadReport(Integer squadId) {
		SquadReport report = squadReports.findOneBySquadIdAndQuarterAndYear(squadId, SrapUtils.currentQuarter(),
				SrapUtils.currentYear());

		if (report == null)
			report = createNewSquadReport(squadId);

		return mapReportToClientBean(report);
	}

	@Override
	public List<SquadReportDO> getSquadsAndReportStatus(Integer domainId, String quarterInput) {
		List<SquadReportDO> response = new ArrayList<>();
		List<Squad> domainSquads = squads.findByDomainId(domainId);

		String quarter = null;
		String year = null;

		boolean quarterInputProvided = false;

		if (quarterInput == null || quarterInput.isEmpty()) {
			quarter = SrapUtils.currentQuarter();
			year = SrapUtils.currentYear();
		} else {
			String[] quarterStringParts = quarterInput.split("Q");
			quarter = quarterStringParts[0];
			year = quarterStringParts[1];
			quarterInputProvided = true;
		}

		for (Squad squad : domainSquads) {
			SquadReportDO bean = new SquadReportDO();
			bean.setSquadId(squad.getId());
			bean.setSquadName(squad.getName());
			bean.setSquadStatus(squad.getStatus().getName());
			bean.setFll(squad.getFll());
			bean.setFp(squad.getFp());
			bean.setSll(squad.getSubdomain().getSll());

			SquadReport report = squadReports.findOneBySquadIdAndQuarterAndYear(squad.getId(), quarter, year);
			if (report != null) {
				bean.setCalculatedRating(report.getCalculatedRating());
				bean.setReportId(report.getId());
				if (report.getFllRating() != null)
					bean.setFllRating(report.getFllRating().getName());
				if (report.getStatus() != null)
					bean.setStep(report.getStatus().getName());
				if (quarterInputProvided) {
					bean.setSavedFp(report.getCurrentFp());
					bean.setSavedFll(report.getCurrentFll());
					bean.setSavedSll(report.getCurrentSll());
				}
				mapHistoryToBean(bean, report);
			}

			response.add(bean);
		}

		return response;
	}

	@Override
	public int createNewReport(EditSquadReportDO bean) {
		return saveReport(bean, true);
	}

	@Override
	public boolean updateReport(EditSquadReportDO bean) {
		return saveReport(bean, false) != 0;
	}

	@Override
	public boolean updateReportStatus(ReportStatusDO bean) {
		if (bean.getReportId() == null || bean.getName() == null)
			return false;

		SquadReport report = squadReports.findOne(bean.getReportId());
		Status status = statuses.findOneByNameIgnoreCase(bean.getName());
		if (report == null || status == null)
			return false;

		report.setStatus(status);
		report.setUpdatedBy((String) httpSession.getAttribute(Messages.LOGGED_IN_USER));
		report = squadReports.save(report);

		return report.getStatus().equals(status);
	}

	private EditSquadReportDO mapReportToClientBean(SquadReport report) {
		EditSquadReportDO bean = new EditSquadReportDO();

		List<ProcessAssessment> assessments = processAssessments.findAllByReportId(report.getId());
		if (assessments == null || assessments.isEmpty()) {
			assessments = createDefaultProcessAssessments(report);
		}

		for (ProcessAssessment assessment : assessments) {
			ProcessRatingDO ratingBean = new ProcessRatingDO();
			ratingBean.setComment(assessment.getComment());
			if (assessment.getProcess() != null) {
				ratingBean.setProcessId(assessment.getProcess().getId());
				ratingBean.setProcessName(assessment.getProcess().getName());
				ratingBean.setDescription(assessment.getProcess().getDescription());
			}
			if (assessment.getRating() != null)
				ratingBean.setRating(assessment.getRating().getValue());
			bean.getProcesses().add(ratingBean);
		}
		if (report.getSquad() != null) {
			bean.setSquadId(report.getSquad().getId());
			bean.setSquadName(report.getSquad().getName());
			bean.setFpName(report.getSquad().getFp());
			bean.setFllName(report.getSquad().getFll());
		}
		bean.setQuarter(report.getQuarter() + "Q" + report.getYear());
		bean.setFllComment(report.getComment());
		bean.setCalculatedRating(report.getCalculatedRating());
		bean.setReportId(report.getId());

		bean.getHistory().setCreatedBy(report.getCreatedBy());
		bean.getHistory().setCreatedTime(report.getCreatedTime());
		bean.getHistory().setLastModifiedBy(report.getUpdatedBy());
		bean.getHistory().setLastModifiedTime(report.getUpdatedTime());

		if (report.getStatus() != null)
			bean.setStatus(report.getStatus().getName());
		if (report.getFllRating() != null)
			bean.setFllRating(report.getFllRating().getValue());

		return bean;
	}

	private int saveReport(EditSquadReportDO bean, boolean createNew) {
		Integer squadId = bean.getSquadId();
		if (squadId == null || !squads.exists(squadId) || bean.getQuarter() == null)
			return 0;

		String quarter = bean.getQuarter().trim().substring(0, 1);
		String year = bean.getQuarter().substring(2).trim();

		SquadReport report = squadReports.findOneBySquadIdAndQuarterAndYear(squadId, quarter, year);
		if (report == null)
			report = new SquadReport();
		report.setSquad(squads.findOne(squadId));
		report.setFllRating(ratings.findOneByValue(bean.getFllRating()));
		report.setCalculatedRating(bean.getCalculatedRating());
		report.setComment(trimComment(bean.getFllComment()));
		report.setQuarter(quarter);
		report.setYear(year);
		report.setStatus(statuses.findOneByNameIgnoreCase(bean.getStatus()));

		if (createNew)
			report.setCreatedBy((String) httpSession.getAttribute(Messages.LOGGED_IN_USER));
		report.setUpdatedBy((String) httpSession.getAttribute(Messages.LOGGED_IN_USER));

		report = squadReports.save(report);

		List<ProcessRatingDO> prbs = bean.getProcesses();
		for (ProcessRatingDO rating : prbs) {
			ProcessAssessment assessment = processAssessments.findOneByProcessIdAndReportId(rating.getProcessId(),
					report.getId());
			if (assessment == null)
				assessment = new ProcessAssessment();
			assessment.setComment(trimComment(rating.getComment()));
			assessment.setProcess(processes.findOne(rating.getProcessId()));
			assessment.setRating(ratings.findOneByValue(rating.getRating()));
			assessment.setReport(report);
			processAssessments.save(assessment);
		}

		return report == null ? 0 : report.getId();
	}

	private String trimComment(String comment) {
		if (comment == null)
			return "";

		if (comment.length() > 1000) {
			String firstPart = comment.substring(0, 983);
			String lastPart = comment.substring(comment.length() - 10, comment.length());
			return firstPart + " [...] " + lastPart;
		}

		return comment;
	}

	private SquadReport createNewSquadReport(Integer squadId) {
		SquadReport report = new SquadReport();
		Squad squad = squads.findOne(squadId);
		if (squad != null) {
			report.setSquad(squad);
			report.setQuarter(SrapUtils.currentQuarter());
			report.setYear(SrapUtils.currentYear());
			report.setStatus(statuses.findOneByNameIgnoreCase("Draft"));
			report.setCreatedBy((String) httpSession.getAttribute(Messages.LOGGED_IN_USER));
			report.setUpdatedBy((String) httpSession.getAttribute(Messages.LOGGED_IN_USER));
			report = squadReports.save(report);
		}
		return report;
	}

	private List<ProcessAssessment> createDefaultProcessAssessments(SquadReport report) {
		Integer domainId = report.getSquad().getSubdomain().getDomain().getId();
		Integer activeStatusId = statuses.findIdByName("Active");
		List<ProcessAssessment> assessments = new ArrayList<>();
		List<Process> activeProcesses = processes.findByDomainIdAndStatusId(domainId, activeStatusId);
		if (activeProcesses == null)
			return assessments;

		for (Process p : activeProcesses) {
			ProcessAssessment pa = new ProcessAssessment();
			pa.setProcess(p);
			pa.setReport(report);
			pa = processAssessments.save(pa);
			assessments.add(pa);
		}

		return assessments;
	}

	private void mapHistoryToBean(SquadReportDO bean, SquadReport report) {
		bean.getHistory().setCreatedBy(report.getCreatedBy());
		bean.getHistory().setCreatedTime(report.getCreatedTime());
		bean.getHistory().setLastModifiedBy(report.getUpdatedBy());
		bean.getHistory().setLastModifiedTime(report.getUpdatedTime());
	}

	@Override
	public FLLRatingGraphDO getFLLratingsGraph(Integer domainId) {

		List<Integer> currentFLLratings = squadReports.findAllCurrentFLLRatingsByDomain(domainId, SrapUtils.currentQuarter(),
				SrapUtils.currentYear());

		FLLRatingGraphDO fllRatingGraphDO = new FLLRatingGraphDO();

		if (!currentFLLratings.isEmpty()) {

			Integer satCount = 0, unsatCount = 0, marginalCount = 0, naCount = 0;

			for (Integer rating : currentFLLratings) {
				switch (rating) {
				case RatingValues.DB_MARGINAL:
					marginalCount++;
					fllRatingGraphDO.setMarginalCount(marginalCount);
					break;
				case RatingValues.DB_SAT:
					satCount++;
					fllRatingGraphDO.setSatCount(satCount);
					break;
				case RatingValues.DB_UNSAT:
					unsatCount++;
					fllRatingGraphDO.setUnsatCount(unsatCount);
					break;
				case RatingValues.DB_NA:
					naCount++;
					fllRatingGraphDO.setNaCount(naCount);
					break;
				}

			}
		}

		return fllRatingGraphDO;
	}

	@Override
	public CalculatedValuesGraphDO getCalculatedRatingValuesGraph(Integer domainId) {

		CalculatedValuesGraphDO calculatedValuesGraphDO = new CalculatedValuesGraphDO();

		if (domainId != null) {
			calculatedValuesGraphDO
					.setSatCount(squads.countSatActiveSquadsByDomainIdAndQuarterAndYear(domainId, SrapUtils.currentQuarter(),
							SrapUtils.currentYear(), RatingValues.MAX_THRESHOLD, RatingValues.SATIS_THRESHOLD));
			calculatedValuesGraphDO.setMarginalCount(
					squads.countMarginalActiveSquadsByDomainIdAndQuarterAndYear(domainId, SrapUtils.currentQuarter(),
							SrapUtils.currentYear(), RatingValues.SATIS_THRESHOLD, RatingValues.MARG_THRESHOLD));
			calculatedValuesGraphDO.setUnsatCount(
					squads.countUnsatActiveSquadsByDomainIdAndQuarterAndYear(domainId, SrapUtils.currentQuarter(),
							SrapUtils.currentYear(), RatingValues.MARG_THRESHOLD, RatingValues.MIN_THRESHOLD));
			calculatedValuesGraphDO.setNotCalculated(squads.countUncalculatedActiveSquadsByDomainIdAndQuarterAndYear(
					domainId, SrapUtils.currentQuarter(), SrapUtils.currentYear()));

		}

		return calculatedValuesGraphDO;
	}

	@Override
	public ProgressGraphDO getProgressGraphValue(Integer domainId) {

		ProgressGraphDO progressGraphDO = new ProgressGraphDO();

		if (domainId != null) {
			progressGraphDO.setDraftCount(squads.countActiveSquadsByDomainIdReportsStepQuarterAndYear(domainId,
					SrapUtils.currentQuarter(), SrapUtils.currentYear(),
					statuses.findOneByNameIgnoreCase(SquadReportSteps.DRAFT.toString()).getId()));

			progressGraphDO.setUntouchedCount(squads.countUntouchedActiveSquadsByDomainId(domainId,
					SrapUtils.currentQuarter(), SrapUtils.currentYear()));

			progressGraphDO.setSignedCount(squads.countActiveSquadsByDomainIdReportsStepQuarterAndYear(domainId,
					SrapUtils.currentQuarter(), SrapUtils.currentYear(),
					statuses.findOneByNameIgnoreCase(SquadReportSteps.SIGNED.toString()).getId()));

			progressGraphDO.setSubmittedCount(squads.countActiveSquadsByDomainIdReportsStepQuarterAndYear(domainId,
					SrapUtils.currentQuarter(), SrapUtils.currentYear(),
					statuses.findOneByNameIgnoreCase(SquadReportSteps.SUBMITTED.toString()).getId()));
		}

		return progressGraphDO;
	}

}
