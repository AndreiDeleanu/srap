package com.ibm.srap.services.implementations;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.srap.client_beans.FllReportsTaskInfoDO;
import com.ibm.srap.client_beans.FpReportsTaskInfoDO;
import com.ibm.srap.client_beans.NotificationDO;
import com.ibm.srap.client_beans.SLLReportsTaskInfoDO;
import com.ibm.srap.models.Deadline;
import com.ibm.srap.models.Domain;
import com.ibm.srap.models.SquadReport;
import com.ibm.srap.models.Status;
import com.ibm.srap.models.SubdomainReport;
import com.ibm.srap.models.repositories.Deadlines;
import com.ibm.srap.models.repositories.Domains;
import com.ibm.srap.models.repositories.SquadReports;
import com.ibm.srap.models.repositories.Statuses;
import com.ibm.srap.models.repositories.SubdomainReports;
import com.ibm.srap.services.NotificationService;
import com.ibm.srap.services.SchedulerService;
import com.ibm.srap.services.utils.Messages;
import com.ibm.srap.services.utils.ReportSteps;
import com.ibm.srap.services.utils.RoleNames;
import com.ibm.srap.services.utils.SrapUtils;

@Service("SchedulerService")
public class SchedulerServiceImpl implements SchedulerService {

	@Autowired
	private Domains domains;
	@Autowired
	private Deadlines deadlines;
	@Autowired
	private SquadReports squadReports;
	@Autowired
	private SubdomainReports subdomainReports;
	@Autowired
	private Statuses statuses;
	@Autowired
	private NotificationService notificationService;

	@Override
	public NotificationDO checkAndNotifyDomainAdmins() {
		List<Domain> dbDomains = domains.findAll();
		if (dbDomains == null)
			return new NotificationDO();

		String currentQuarter = SrapUtils.currentQuarter() + "Q" + SrapUtils.currentYear();
		List<String> recipients = new ArrayList<>();

		for (Domain domain : dbDomains) {
			if (!deadlines.existsByDomainIdAndQuarter(domain.getId(), currentQuarter))
				recipients.add(domain.getAdmin());
		}

		if (recipients.isEmpty())
			return new NotificationDO();

		NotificationDO notification = new NotificationDO();
		notification.getRecipients().addAll(recipients);
		notification.setContact(Messages.NOTIFICATION_SENDER);
		notification.setSubject(Messages.ADMIN_NOTIFICATION_SUBJECT);
		notification.setMessage(Messages.ADMIN_NOTIFICATION_MESSAGE);

		notificationService.sendNotification(notification);

		return notification;
	}

	@Override
	public FpReportsTaskInfoDO checkAndSubmitSquadReports() {
		final Status stepNew = statuses.findOneByNameIgnoreCase(ReportSteps.UNTOUCHED.name());
		final Status stepDraft = statuses.findOneByNameIgnoreCase(ReportSteps.DRAFT.name());
		final Status stepSubmitted = statuses.findOneByNameIgnoreCase(ReportSteps.SUBMITTED.name());

		List<SquadReport> newOrDraftReports = new ArrayList<>();
		newOrDraftReports.addAll(squadReports.findAllByStatus(stepNew));
		newOrDraftReports.addAll(squadReports.findAllByStatus(stepDraft));

		if (newOrDraftReports.isEmpty())
			return new FpReportsTaskInfoDO();

		String currentQuarter = SrapUtils.currentQuarter() + "Q" + SrapUtils.currentYear();
		FpReportsTaskInfoDO response = new FpReportsTaskInfoDO();
		long days = 0;

		for (SquadReport report : newOrDraftReports) {
			initializeSquadHibernateProxys(report);
			Integer domainId = report.getSquad().getSubdomain().getDomain().getId();
			Deadline deadline = deadlines.findOneByDomainIdAndQuarter(domainId, currentQuarter);
			if (deadline == null)
				continue;
			Timestamp deadlineTime = deadline.getFpSubmitDeadline();
			if (hasReachedDeadline(deadlineTime)) {
				report.setStatus(stepSubmitted);
				report.setUpdatedBy(Messages.SRAP_PORTAL_SCHEDULED);
				squadReports.save(report);
				response.getSquadReportsToSubmit().add(report.getId());
			}
			days = getDaysUntil(deadlineTime);
			if (days == 5 || days == 2) {
				response.getFpsToNotify().add(report.getSquad().getFp());
			}
		}

		notifyPersons(response.getFpsToNotify(), days, RoleNames.FP);

		return response;
	}

	@Override
	public FllReportsTaskInfoDO checkAndSignSquadReports() {
		final Status stepSubmitted = statuses.findOneByNameIgnoreCase(ReportSteps.SUBMITTED.name());
		final Status stepSigned = statuses.findOneByNameIgnoreCase(ReportSteps.SIGNED.name());

		List<SquadReport> submittedReports = squadReports.findAllByStatus(stepSubmitted);
		if (submittedReports == null)
			return new FllReportsTaskInfoDO();

		String currentQuarter = SrapUtils.currentQuarter() + "Q" + SrapUtils.currentYear();
		FllReportsTaskInfoDO response = new FllReportsTaskInfoDO();
		long days = 0;

		for (SquadReport report : submittedReports) {
			initializeSquadHibernateProxys(report);
			Integer domainId = report.getSquad().getSubdomain().getDomain().getId();
			Deadline deadline = deadlines.findOneByDomainIdAndQuarter(domainId, currentQuarter);
			if (deadline == null)
				continue;
			Timestamp deadlineTime = deadline.getFllSignoffDeadline();
			if (hasReachedDeadline(deadlineTime)) {
				report.setStatus(stepSigned);
				report.setUpdatedBy(Messages.SRAP_PORTAL_SCHEDULED);
				squadReports.save(report);
				response.getSquadReportsToSign().add(report.getId());
			}
			days = getDaysUntil(deadlineTime);
			if (days == 5 || days == 2) {
				response.getFllsToNotify().add(report.getSquad().getFll());
			}
		}

		notifyPersons(response.getFllsToNotify(), days, RoleNames.FLL);

		return response;
	}

	@Override
	public SLLReportsTaskInfoDO checkAndSubmitSubdomainReports() {
		final Status stepSubmitted = statuses.findOneByNameIgnoreCase(ReportSteps.SUBMITTED.name());
		final Status stepNew = statuses.findOneByNameIgnoreCase(ReportSteps.UNTOUCHED.name());
		final Status stepDraft = statuses.findOneByNameIgnoreCase(ReportSteps.DRAFT.name());

		List<SubdomainReport> newOrDraftReports = new ArrayList<>();
		newOrDraftReports.addAll(subdomainReports.findAllByStatus(stepNew));
		newOrDraftReports.addAll(subdomainReports.findAllByStatus(stepDraft));

		if (newOrDraftReports.isEmpty())
			return new SLLReportsTaskInfoDO();

		String currentQuarter = SrapUtils.currentQuarter() + "Q" + SrapUtils.currentYear();
		SLLReportsTaskInfoDO response = new SLLReportsTaskInfoDO();
		long days = 0;

		for (SubdomainReport report : newOrDraftReports) {
			initializeSubdomainHibernateProxys(report);
			Integer domainId = report.getSubdomain().getDomain().getId();
			Deadline deadline = deadlines.findOneByDomainIdAndQuarter(domainId, currentQuarter);
			if (deadline == null)
				continue;
			Timestamp deadlineTime = deadline.getSllRollupDeadline();
			if (hasReachedDeadline(deadlineTime)) {
				report.setStatus(stepSubmitted);
				report.setUpdatedBy(Messages.SRAP_PORTAL_SCHEDULED);
				subdomainReports.save(report);
				response.getSubdomainReportsToSubmit().add(report.getId());
			}
			days = getDaysUntil(deadlineTime);
			if (days == 5 || days == 2) {
				response.getSllsToNotify().add(report.getSubdomain().getSll());
			}
		}

		notifyPersons(response.getSllsToNotify(), days, RoleNames.SLL);

		return response;
	}

	private void initializeSubdomainHibernateProxys(SubdomainReport report) {
		Hibernate.initialize(report.getSubdomain());
		Hibernate.initialize(report.getSubdomain().getDomain());
	}

	private void initializeSquadHibernateProxys(SquadReport report) {
		Hibernate.initialize(report.getSquad());
		Hibernate.initialize(report.getSquad().getSubdomain());
		Hibernate.initialize(report.getSquad().getSubdomain().getDomain());
	}

	private boolean hasReachedDeadline(Timestamp target) {
		Instant deadline = target.toInstant();
		Instant now = Instant.now();
		return now.isAfter(deadline);
	}

	private long getDaysUntil(Timestamp target) {
		LocalDate now = LocalDate.now();
		LocalDate deadline = target.toLocalDateTime().toLocalDate();
		return ChronoUnit.DAYS.between(now, deadline);
	}

	private void notifyPersons(List<String> recipients, long days, RoleNames role) {
		if (recipients.isEmpty())
			return;
		NotificationDO notification = new NotificationDO();
		notification.setSubject(Messages.ACTION_REQUIRED_SUBJECT);
		notification.setContact(Messages.NOTIFICATION_SENDER);
		notification.getRecipients().addAll(recipients);
		notification.getBcc().add("mmesesan@ro.ibm.com");
		String daysLeft = String.format(Messages.X_DAYS_REMAINING, days);
		if (RoleNames.SLL.equals(role))
			notification.setMessage(Messages.SLL_NOTIFICATION_MESSAGE + daysLeft);
		if (RoleNames.FP.equals(role))
			notification.setMessage(Messages.FP_NOTIFICATION_MESSAGE + daysLeft);
		if (RoleNames.FLL.equals(role))
			notification.setMessage(Messages.FLL_NOTIFICATION_MESSAGE + daysLeft);

		notificationService.sendNotification(notification);
	}

	@Override
	public void storeRolesQuarterly() {

		List<SquadReport> squadReportsList = squadReports.findAllByQuarterAndYear(SrapUtils.currentQuarter(),
				SrapUtils.currentYear());

		if (!squadReportsList.isEmpty()) {
			for (SquadReport squadReport : squadReportsList) {
				squadReport.setCurrentFll(squadReport.getSquad().getFll());
				squadReport.setCurrentFp(squadReport.getSquad().getFp());
				squadReport.setCurrentSll(squadReport.getSquad().getSubdomain().getSll());
				squadReports.save(squadReport);
			}
		}

		List<SubdomainReport> subdomainReportList = subdomainReports.findAllByQuarterAndYear(SrapUtils.currentQuarter(),
				SrapUtils.currentYear());

		if (!subdomainReportList.isEmpty()) {
			for (SubdomainReport subdomainReport : subdomainReportList) {
				subdomainReport.setCurrentSll(subdomainReport.getSubdomain().getSll());
				subdomainReports.save(subdomainReport);
			}
		}

	}

}
