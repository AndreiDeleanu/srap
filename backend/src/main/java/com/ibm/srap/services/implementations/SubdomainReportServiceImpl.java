package com.ibm.srap.services.implementations;

import static com.ibm.srap.services.utils.SrapUtils.currentQuarter;
import static com.ibm.srap.services.utils.SrapUtils.currentYear;
import static com.ibm.srap.services.utils.SrapUtils.hasData;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.srap.client_beans.OperationResult;
import com.ibm.srap.client_beans.ReportStatusDO;
import com.ibm.srap.client_beans.SubdomainProcessAssessmentDO;
import com.ibm.srap.client_beans.SubdomainReportDO;
import com.ibm.srap.client_beans.SubdomainReportSummaryDO;
import com.ibm.srap.models.Domain;
import com.ibm.srap.models.Process;
import com.ibm.srap.models.Status;
import com.ibm.srap.models.Subdomain;
import com.ibm.srap.models.SubdomainProcessAssessment;
import com.ibm.srap.models.SubdomainReport;
import com.ibm.srap.models.repositories.ProcessAssessments;
import com.ibm.srap.models.repositories.Processes;
import com.ibm.srap.models.repositories.Ratings;
import com.ibm.srap.models.repositories.SquadReports;
import com.ibm.srap.models.repositories.Squads;
import com.ibm.srap.models.repositories.Statuses;
import com.ibm.srap.models.repositories.SubdomainProcessAssessments;
import com.ibm.srap.models.repositories.SubdomainReports;
import com.ibm.srap.models.repositories.Subdomains;
import com.ibm.srap.services.SubdomainReportService;
import com.ibm.srap.services.ThresholdService;
import com.ibm.srap.services.utils.Messages;
import com.ibm.srap.services.utils.RatingValues;
import com.ibm.srap.services.utils.ReportSteps;

@Service("SubdomainReportService")
public class SubdomainReportServiceImpl implements SubdomainReportService {

	@Autowired
	private Subdomains subdomains;
	@Autowired
	private SubdomainReports subdomainReports;
	@Autowired
	private SubdomainProcessAssessments subdomainProcessAssessments;
	@Autowired
	private SquadReports squadReports;
	@Autowired
	private Processes processes;
	@Autowired
	private Ratings ratings;
	@Autowired
	private ProcessAssessments processAssessments;
	@Autowired
	private Statuses statuses;
	@Autowired
	private HttpSession httpSession;
	@Autowired
	private Squads squads;
	@Autowired
	private ThresholdService thresholdService;

	@Override
	public List<SubdomainReportDO> getSubdomainReportsFor(Integer subdomainId) {
		return getSubdomainReportsBy(subdomainId, false);
	}

	@Override
	public List<SubdomainReportDO> getCurrentQuarterReportFor(Integer subdomainId) {
		return getSubdomainReportsBy(subdomainId, true);
	}

	@Override
	public OperationResult saveReport(SubdomainReportDO input) {
		if (input.getSubdomainId() == null)
			return new OperationResult(false, Messages.NO_SUBDOMAIN_ID);

		Subdomain subdomain = subdomains.findOne(input.getSubdomainId());
		if (subdomain == null)
			return new OperationResult(false, Messages.SUBDOMAIN_NOT_FOUND);

		SubdomainReport report = subdomainReports.findOne(input.getId());
		if (report == null) {
			report = new SubdomainReport();
			report.setStatus(statuses.findOneByNameIgnoreCase(ReportSteps.DRAFT.toString()));
			report.setQuarter(currentQuarter());
			report.setYear(currentYear());
			report.setCreatedBy((String) httpSession.getAttribute(Messages.LOGGED_IN_USER));
		}

		if (report.getSubdomain() == null)
			report.setSubdomain(subdomain);

		if (hasData(input.getSllOverride()))
			report.setSllOverride(ratings.findOneByName(input.getSllOverride()));
		if (hasData(input.getSllComment()))
			report.setSllComment(input.getSllComment());
		if (hasData(input.getCalculatedValue()))
			report.setCalculatedValue(input.getCalculatedValue());
		if (hasData(input.getQuarter()))
			report.setQuarter(input.getQuarter());
		if (hasData(input.getYear()))
			report.setYear(input.getYear());
		if (hasData(input.getStep()))
			report.setStatus(statuses.findOneByNameIgnoreCase(input.getStep()));

		// check if the subdomain has squad for calculations

		boolean foundSquad = squads.existsBySubdomainId(subdomain.getId());

		if (foundSquad) {
			Integer fllTotal = 0;
			Integer fllAssessments = 0;
			List<Integer> fllAverages = squadReports.findAllCurrentFLLRatingsBySubdomain(input.getSubdomainId(),
					currentQuarter(), currentYear());

			for (Integer value : fllAverages) {
				if (value != null && value != RatingValues.DB_NA) {
					if (value == RatingValues.DB_SAT)
						fllTotal += RatingValues.ADJUSTED_SAT;
					if (value == RatingValues.DB_MARGINAL)
						fllTotal += RatingValues.ADJUSTED_MARGINAL;
					if (value == RatingValues.DB_UNSAT)
						fllTotal += RatingValues.ADJUSTED_UNSAT;
					fllAssessments++;
				}
			}

			if (fllAssessments > 0) {
				report.setFllAverage(RatingValues.calculatePercentage(fllTotal, fllAssessments));
			}
		} else {
			report.setFllAverage(null);
		}

		report.setUpdatedBy((String) httpSession.getAttribute(Messages.LOGGED_IN_USER));
		report = subdomainReports.save(report);

		// calculate process averages for squad reports process assessments in the
		// specified domain
		for (SubdomainProcessAssessmentDO paBean : input.getProcessAssessments()) {
			SubdomainProcessAssessment spa = subdomainProcessAssessments.findOneByProcessIdAndReportId(paBean.getId(),
					report.getId());
			if (spa == null)
				spa = new SubdomainProcessAssessment();

			Process p = processes.findOne(paBean.getId());
			if (p != null)
				spa.setProcess(p);

			spa.setReport(report);

			if (foundSquad) {
				Integer prTotal = 0;
				Integer prAssessments = 0;
				List<Integer> processAverages = processAssessments.findAllCurrentProcessAssessmentsBySubdomain(
						paBean.getProcessId(), paBean.getSubdomainId(), currentQuarter(), currentYear());
				for (Integer value : processAverages) {
					if (value != null && value != RatingValues.DB_NA) {
						if (value == RatingValues.DB_SAT)
							prTotal += RatingValues.ADJUSTED_SAT;
						if (value == RatingValues.DB_MARGINAL)
							prTotal += RatingValues.ADJUSTED_MARGINAL;
						if (value == RatingValues.DB_UNSAT)
							prTotal += RatingValues.ADJUSTED_UNSAT;
						prAssessments++;
					}
				}

				if (prAssessments == 0) {
					for (Integer value : processAverages) {
						if (value == RatingValues.DB_NA) {
							spa.setAverage(ratings.findOneByValue(RatingValues.DB_NA));
							subdomainProcessAssessments.save(spa);
							break;
						}

					}

				} else {
					spa.setAverage(ratings.findOneByValue(RatingValues.averageValues(prTotal, prAssessments,
							thresholdService.getThresholdsByDomainIdForCurrentQuarterAndYear(input.getDomainId()))));
					subdomainProcessAssessments.save(spa);
				}
			} else {
				spa.setAverage(null);
				subdomainProcessAssessments.save(spa);
			}

		}
		return new OperationResult(true, Messages.ENTITY_SAVED);
	}

	@Override
	public boolean updateReportStatus(ReportStatusDO bean) {
		if (bean.getReportId() == null || bean.getName() == null)
			return false;

		SubdomainReport report = subdomainReports.findOne(bean.getReportId());
		Status status = statuses.findOneByNameIgnoreCase(bean.getName());
		if (report == null || status == null)
			return false;

		report.setStatus(status);
		report.setUpdatedBy((String) httpSession.getAttribute(Messages.LOGGED_IN_USER));
		report = subdomainReports.save(report);

		return report.getStatus().equals(status);
	}

	@Override
	public SubdomainReportDO getReport(int reportId) {
		SubdomainReport report = subdomainReports.findOne(reportId);
		if (report == null)
			return new SubdomainReportDO();

		return mapEntity(report);
	}

	@Override
	public List<SubdomainReportSummaryDO> getReportsBySubdomain(Integer subdomainId, String quarterInput) {
		String quarter = quarterInput == null ? currentQuarter() : quarterInput.split("Q")[0];
		String year = quarterInput == null ? currentYear() : quarterInput.split("Q")[1];
		List<SubdomainReport> reports = subdomainReports.findAllBySubdomainIdAndQuarterAndYear(subdomainId, quarter, year);

		List<SubdomainReportSummaryDO> response = new ArrayList<>();
		for (SubdomainReport report : reports) {
			response.add(mapEntityToSummaryDO(report));
		}

		return response;
	}

	@Override
	public List<SubdomainReportSummaryDO> getReportsByDomain(Integer domainId, String quarterInput) {
		String quarter = quarterInput == null ? currentQuarter() : quarterInput.split("Q")[0];
		String year = quarterInput == null ? currentYear() : quarterInput.split("Q")[1];
		List<SubdomainReport> reports = subdomainReports.findAllBySubdomainDomainIdAndQuarterAndYear(domainId, quarter,
				year);

		List<SubdomainReportSummaryDO> response = new ArrayList<>();
		for (SubdomainReport report : reports) {
			response.add(mapEntityToSummaryDO(report));
		}

		return response;
	}

	private SubdomainReportSummaryDO mapEntityToSummaryDO(SubdomainReport report) {
		SubdomainReportSummaryDO bean = new SubdomainReportSummaryDO();
		bean.setSubdomainId(report.getSubdomain().getId());
		bean.setSubdomainName(report.getSubdomain().getName());
		bean.setFllAverage(report.getFllAverage());
		bean.setCalculated(report.getCalculatedValue());
		if (report.getSllOverride() != null)
			bean.setSllOverride(report.getSllOverride().getName());
		bean.setSubdomainReportId(report.getId());
		bean.setSllEmail(report.getSubdomain().getSll());
		return bean;
	}

	private List<SubdomainReportDO> getSubdomainReportsBy(Integer subdomainId, boolean getCurrent) {
		List<SubdomainReportDO> response = new ArrayList<>();
		if (!subdomains.exists(subdomainId))
			return response;

		List<SubdomainReport> subReports = new ArrayList<>();

		if (getCurrent) {
			subReports.addAll(
					subdomainReports.findAllBySubdomainIdAndQuarterAndYear(subdomainId, currentQuarter(), currentYear()));
		} else {
			subReports.addAll(subdomainReports.findAllBySubdomainId(subdomainId));
		}

		for (SubdomainReport report : subReports) {
			response.add(mapEntity(report));
		}

		return response;
	}

	private SubdomainReportDO mapEntity(SubdomainReport report) {
		SubdomainReportDO bean = new SubdomainReportDO();
		mapReportFields(bean, report);
		if (report.getSubdomain() != null)
			mapSubdomainFields(bean, report.getSubdomain());
		if (report.getStatus() != null)
			bean.setStep(report.getStatus().getName());
		if (report.getSllOverride() != null)
			bean.setSllOverride(report.getSllOverride().getName());

		List<SubdomainProcessAssessment> sPAs = subdomainProcessAssessments.findAllByReportId(report.getId());
		if (sPAs != null)
			bean.getProcessAssessments().addAll(mapAssessments(sPAs));

		bean.getHistory().setCreatedBy(report.getCreatedBy());
		bean.getHistory().setCreatedTime(report.getCreatedTime());
		bean.getHistory().setLastModifiedBy(report.getUpdatedBy());
		bean.getHistory().setLastModifiedTime(report.getUpdatedTime());

		return bean;
	}

	private void mapReportFields(SubdomainReportDO bean, SubdomainReport report) {
		bean.setCalculatedValue(report.getCalculatedValue());
		bean.setFllAverage(report.getFllAverage());
		bean.setQuarter(report.getQuarter());
		bean.setYear(report.getYear());
		bean.setId(report.getId());
		bean.setSllComment(report.getSllComment());
	}

	private void mapSubdomainFields(SubdomainReportDO bean, Subdomain subdomain) {
		bean.setSllEmail(subdomain.getSll());
		bean.setSubdomainName(subdomain.getName());
		bean.setSubdomainId(subdomain.getId());
		if (subdomain.getDomain() != null)
			mapDomainFields(bean, subdomain.getDomain());
	}

	private void mapDomainFields(SubdomainReportDO bean, Domain domain) {
		bean.setDomainId(domain.getId());
		bean.setDomainName(domain.getName());
	}

	private List<SubdomainProcessAssessmentDO> mapAssessments(List<SubdomainProcessAssessment> sPAs) {
		List<SubdomainProcessAssessmentDO> processBeans = new ArrayList<>();
		for (SubdomainProcessAssessment sPA : sPAs) {
			SubdomainProcessAssessmentDO bean = new SubdomainProcessAssessmentDO();
			bean.setId(sPA.getId());
			if (sPA.getAverage() != null)
				bean.setAverage(sPA.getAverage().getName());
			if (sPA.getProcess() != null) {
				bean.setProcessId(sPA.getProcess().getId());
				bean.setProcessName(sPA.getProcess().getName());
			}
			if (sPA.getReport() != null)
				bean.setSubdomainId(sPA.getReport().getId());
			processBeans.add(bean);
		}
		return processBeans;
	}

}
