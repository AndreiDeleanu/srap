package com.ibm.srap.services.utils;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ibm.srap.client_beans.NotificationDO;
import com.ibm.srap.client_beans.SchedulerInfoDO;
import com.ibm.srap.services.SchedulerService;

/**
 * @author MihaiMesesan
 *
 *         Automated task to enable user notifications
 */
@Component
@PropertySource("classpath:automated_task.properties")
public class ScheduleTask {

	private static final Logger logger = Logger.getLogger(ScheduleTask.class.getName());

	@Autowired
	private SchedulerService scheduler;

	@Scheduled(cron = "${daily_task.schedule}", zone = "${automated_task.timezone}")
	public SchedulerInfoDO dailyValidation() {
		logger.log(Level.INFO, "Starting daily validation automated task");
		SchedulerInfoDO response = new SchedulerInfoDO();
		response.setTaskInfoForFPs(scheduler.checkAndSubmitSquadReports());
		response.setTaskInfoForFLLs(scheduler.checkAndSignSquadReports());
		response.setTaskInfoForSLLs(scheduler.checkAndSubmitSubdomainReports());
		logger.log(Level.INFO, "FLLs to notify: {0}", response.getTaskInfoForFLLs().getFllsToNotify().size());
		logger.log(Level.INFO, "FPs to notify: {0}", response.getTaskInfoFPs().getFpsToNotify().size());
		logger.log(Level.INFO, "SLLs to notify: {0}", response.getTaskInfoForSLLs().getSllsToNotify().size());
		return response;
	}

	@Scheduled(cron = "${quarterly_task.schedule}", zone = "${automated_task.timezone}")
	public NotificationDO quarterlyValidation() {
		logger.log(Level.INFO, "Starting quarterly validation automated task");
		NotificationDO response = scheduler.checkAndNotifyDomainAdmins();
		logger.log(Level.INFO, "Admins to notify: {0}", response.getRecipients().size());
		return response;
	}

	@Scheduled(cron = "${quarterly_lastDay_task.schedule}", zone = "${automated_task.timezone}")
	public void quarterlyStoreRoles() {
		final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("${automated_task.timezone}"));
		if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
			logger.log(Level.INFO, "Starting quarterly store roles");
			scheduler.storeRolesQuarterly();
			logger.log(Level.INFO, "End quarterly store roles");
		}

	}

}
