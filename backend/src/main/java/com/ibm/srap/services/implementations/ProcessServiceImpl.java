package com.ibm.srap.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ibm.srap.client_beans.OperationResult;
import com.ibm.srap.client_beans.ProcessDO;
import com.ibm.srap.client_beans.ProcessDashboardDO;
import com.ibm.srap.client_beans.SearchFilterParamsDO;
import com.ibm.srap.models.Domain;
import com.ibm.srap.models.Process;
import com.ibm.srap.models.ProcessDashboard;
import com.ibm.srap.models.Status;
import com.ibm.srap.models.repositories.Domains;
import com.ibm.srap.models.repositories.Processes;
import com.ibm.srap.models.repositories.ProcessesDashboard;
import com.ibm.srap.models.repositories.Ratings;
import com.ibm.srap.models.repositories.Statuses;
import com.ibm.srap.services.ProcessService;
import com.ibm.srap.services.utils.SrapUtils;

@Service("ProcessService")
public class ProcessServiceImpl implements ProcessService {

	@Autowired
	Processes processes;
	@Autowired
	Statuses statuses;
	@Autowired
	Domains domains;
	@Autowired
	ProcessesDashboard processesDashboard;
	@Autowired
	Ratings ratings;
	@Autowired
	EntityManager entityManger;
	@Autowired
	Environment env;

	@Override
	public List<ProcessDO> getDomainProcesses(Integer domainId) {
		List<ProcessDO> processBeans = new ArrayList<>();
		List<Process> domainProcesses = processes.findByDomainId(domainId);
		if (domainProcesses == null)
			return processBeans;

		for (Process entity : domainProcesses) {
			ProcessDO bean = new ProcessDO();
			bean.setId(entity.getId());
			bean.setDescription(entity.getDescription());
			bean.setName(entity.getName());
			if (entity.getStatus() != null)
				bean.setStatus(entity.getStatus().getName());
			bean.setDomainId(entity.getDomain().getId());
			bean.setWeight(entity.getRiskWeight());
			processBeans.add(bean);
		}
		return processBeans;
	}

	@Override
	public ProcessDO getProcessById(int id) {
		Process entity = processes.findOne(id);
		if (entity != null) {
			return new ModelMapper().map(entity, ProcessDO.class);
		} else {
			return null;
		}
	}

	@Override
	public boolean createNewProcess(ProcessDO processBean) {
		if (processBean == null)
			return false;

		Process entity = new ModelMapper().map(processBean, Process.class);
		entity.setRiskWeight(processBean.getWeight());

		Domain domain = domains.findOne(processBean.getDomainId());
		entity.setDomain(domain);

		Status status = statuses.findOne(Integer.parseInt(processBean.getStatus()));
		entity.setStatus(status);
		entity = processes.save(entity);

		return (entity.getId() != null);
	}

	@Override
	public OperationResult updateProcess(ProcessDO pb) {
		if (pb.getId() == null)
			return new OperationResult(false, "No process id provided");

		Process process = processes.findOne(pb.getId());
		if (process == null)
			return new OperationResult(false, "No process found with this id");

		if (!pb.getName().isEmpty())
			process.setName(pb.getName());
		if (!pb.getDescription().isEmpty())
			process.setDescription(pb.getDescription());
		if (pb.getWeight() != null)
			process.setRiskWeight(pb.getWeight());
		if (!pb.getStatus().isEmpty()) {
			Status status = statuses.findOneByNameIgnoreCase(pb.getStatus());
			if (status == null)
				return new OperationResult(false, "No status found with this name");
			process.setStatus(status);
		}
		if (pb.getDomainId() != null) {
			Domain domain = domains.findOne(pb.getDomainId());
			if (domain == null)
				return new OperationResult(false, "No domain found with this id");
			process.setDomain(domain);
		}

		processes.save(process);
		return new OperationResult(true, "Process updated");
	}

	@Override
	public List<ProcessDashboardDO> getProcessesByDomainForCurrentQuarter(Integer domainId) {
		List<ProcessDashboardDO> processDashboardDOs = new ArrayList<>();

		List<ProcessDashboard> processDashboards = processesDashboard.findAllByDomainIdAndQuarterAndYear(domainId,
				SrapUtils.currentQuarter(), SrapUtils.currentYear());

		if (processDashboards == null)
			return processDashboardDOs;

		for (ProcessDashboard processDashboardEntity : processDashboards) {
			ProcessDashboardDO processDashboardDO = new ProcessDashboardDO();
			processDashboardDO.setId(processDashboardEntity.getId());
			processDashboardDO.setProcessName(processDashboardEntity.getProcessName());
			processDashboardDO.setSquadName(processDashboardEntity.getSquadName());
			processDashboardDO.setSubdomainName(processDashboardEntity.getSubdomainName());
			processDashboardDO.setFp_email(processDashboardEntity.getFp_email());
			processDashboardDO.setSll_email(processDashboardEntity.getSll_email());
			processDashboardDO.setFll_email(processDashboardEntity.getFll_email());
			if (processDashboardEntity.getRating() != null) {
				processDashboardDO.setRating(ratings.findOne(processDashboardEntity.getRating()).getName());
			}
			if (processDashboardEntity.getComment() != null) {
				processDashboardDO.setComment(processDashboardEntity.getComment());
			}

			processDashboardDOs.add(processDashboardDO);
		}

		return processDashboardDOs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProcessDashboardDO> getProcessesByDomainIdAndFilters(SearchFilterParamsDO searchFilterParamsDO) {

		List<ProcessDashboard> processesFiltered = new ArrayList<ProcessDashboard>();
		List<ProcessDashboardDO> processesFilteredDO = new ArrayList<ProcessDashboardDO>();
		StringBuilder queryString = new StringBuilder();

		if (searchFilterParamsDO.getDomainId() != null) {

			queryString.append("SELECT *  from " + env.getProperty("spring.jpa.properties.hibernate.default_schema")
					+ ".process_dashboard as pd ");
			queryString.append("WHERE (pd.domain_id = :domainId) ");
			queryString.append(" AND (pd.quarter = :quarter)");
			queryString.append(" AND (pd.year = :year)");

			if (!searchFilterParamsDO.getSquadNames().isEmpty()) {

				queryString.append(" AND ");
				queryString.append("(LCASE(pd.squad_name) IN( ");
				queryString.append("'" + searchFilterParamsDO.getSquadNames().get(0) + "'");

				for (int i = 1; i < searchFilterParamsDO.getSquadNames().size(); i++) {
					queryString.append(",");
					queryString.append("'" + searchFilterParamsDO.getSquadNames().get(i) + "'");
				}

				queryString.append(")) ");

			}

			if (!searchFilterParamsDO.getSubdomainNames().isEmpty()) {

				queryString.append(" AND ");
				queryString.append("(LCASE(pd.subdomain_name) IN( ");
				queryString.append("'" + searchFilterParamsDO.getSubdomainNames().get(0) + "'");

				for (int i = 1; i < searchFilterParamsDO.getSubdomainNames().size(); i++) {
					queryString.append(",");
					queryString.append("'" + searchFilterParamsDO.getSubdomainNames().get(i) + "'");
				}

				queryString.append(")) ");

			}

			if (!searchFilterParamsDO.getProcessNames().isEmpty()) {

				queryString.append(" AND ");
				queryString.append("(LCASE(pd.process_name) IN( ");
				queryString.append("'" + searchFilterParamsDO.getProcessNames().get(0) + "'");

				for (int i = 1; i < searchFilterParamsDO.getProcessNames().size(); i++) {
					queryString.append(",");
					queryString.append("'" + searchFilterParamsDO.getProcessNames().get(i) + "'");
				}

				queryString.append(")) ");

			}

			if (!searchFilterParamsDO.getRatings().isEmpty()) {

				queryString.append(" AND ");
				queryString.append("(pd.rating IN( ");
				queryString.append("'" + searchFilterParamsDO.getRatings().get(0) + "'");

				for (int i = 1; i < searchFilterParamsDO.getRatings().size(); i++) {
					queryString.append(",");
					queryString.append("'" + searchFilterParamsDO.getRatings().get(i) + "'");
				}

				queryString.append(")) ");

			}

		}

		Query q = entityManger.createNativeQuery(queryString.toString(), ProcessDashboard.class);
		q.setParameter("domainId", searchFilterParamsDO.getDomainId());
		q.setParameter("quarter", SrapUtils.currentQuarter());
		q.setParameter("year", SrapUtils.currentYear());

		processesFiltered = q.getResultList();

		for (ProcessDashboard processDashboard : processesFiltered) {
			ProcessDashboardDO processDashboardDO = new ProcessDashboardDO();
			processDashboardDO.setId(processDashboard.getId());
			processDashboardDO.setProcessName(processDashboard.getProcessName());
			processDashboardDO.setSquadName(processDashboard.getSquadName());
			processDashboardDO.setSubdomainName(processDashboard.getSubdomainName());
			processDashboardDO.setFp_email(processDashboard.getFp_email());
			processDashboardDO.setSll_email(processDashboard.getSll_email());
			processDashboardDO.setFll_email(processDashboard.getFll_email());
			if (processDashboard.getRating() != null) {
				processDashboardDO.setRating(ratings.findOne(processDashboard.getRating()).getName());
			}
			if (processDashboard.getComment() != null) {
				processDashboardDO.setComment(processDashboard.getComment());
			}

			processesFilteredDO.add(processDashboardDO);

		}

		return processesFilteredDO;
	}

	@Override
	public XSSFWorkbook exportXLSX(SearchFilterParamsDO searchFilterParamsDO) {

		List<ProcessDashboardDO> dashboardDOs = getProcessesByDomainIdAndFilters(searchFilterParamsDO);

		XSSFWorkbook workbook = new XSSFWorkbook();

		AtomicInteger rowCount = new AtomicInteger(1);

		XSSFSheet sheet = workbook.createSheet();
		createHeader(workbook, sheet);

		dashboardDOs.forEach(dashboardObject -> {
			Row row = sheet.createRow(rowCount.getAndIncrement());

			Cell cell = row.createCell(0);
			cell.setCellValue(rowCount.get() - 1);

			cell = row.createCell(1);
			cell.setCellValue(dashboardObject.getProcessName());

			cell = row.createCell(2);
			cell.setCellValue(dashboardObject.getSquadName());

			cell = row.createCell(3);
			cell.setCellValue(dashboardObject.getSubdomainName());

			cell = row.createCell(4);
			cell.setCellValue(dashboardObject.getRating());

			cell = row.createCell(5);
			cell.setCellValue(dashboardObject.getComment());

			cell = row.createCell(6);
			cell.setCellValue(dashboardObject.getFp_email());

			cell = row.createCell(7);
			cell.setCellValue(dashboardObject.getFll_email());

			cell = row.createCell(8);
			cell.setCellValue(dashboardObject.getSll_email());

		});

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(5);
		sheet.autoSizeColumn(6);
		sheet.autoSizeColumn(7);
		sheet.autoSizeColumn(8);

		return workbook;
	}

	private void createHeader(XSSFWorkbook workbook, XSSFSheet sheet) {

		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		style.setFont(font);

		Row row = sheet.createRow(0);

		Cell cell = row.createCell(0);

		cell.setCellValue("#");
		cell.setCellStyle(style);

		cell = row.createCell(1);
		cell.setCellValue("PROCESS NAME");
		cell.setCellStyle(style);

		cell = row.createCell(2);
		cell.setCellValue("SQUAD NAME");
		cell.setCellStyle(style);

		cell = row.createCell(3);
		cell.setCellValue("SUBDOMAIN NAME");
		cell.setCellStyle(style);

		cell = row.createCell(4);
		cell.setCellValue("RATING");
		cell.setCellStyle(style);

		cell = row.createCell(5);
		cell.setCellValue("COMMENT");
		cell.setCellStyle(style);

		cell = row.createCell(6);
		cell.setCellValue("FP EMAIL");
		cell.setCellStyle(style);

		cell = row.createCell(7);
		cell.setCellValue("FLL EMAIL");
		cell.setCellStyle(style);

		cell = row.createCell(8);
		cell.setCellValue("SLL EMAIL");
		cell.setCellStyle(style);
	}

}
