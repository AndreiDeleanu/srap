package com.ibm.srap.controllers;

import static com.ibm.srap.services.utils.Messages.JSON;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.srap.client_beans.DefaultResponse;
import com.ibm.srap.client_beans.ReportStatusDO;
import com.ibm.srap.client_beans.SquadReportDO;
import com.ibm.srap.client_beans.EditSquadReportDO;
import com.ibm.srap.services.SquadReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController @RequestMapping("/squads/reports")
@PropertySource("classpath:swagger.properties")
@Api(tags = "Squad Reports")
public class SquadReportsController {
	
	@Autowired private SquadReportService squadReportsManagement;
	
	
	@GetMapping(produces = JSON) @ApiOperation("${squad_reports.get_all_description}")
	public ResponseEntity<List<SquadReportDO>> getDomainReports(
			@ApiParam("${squad_reports.domain_input}") @RequestParam(name="domainId", required=true) Integer domainId, 
			@ApiParam("${squad_reports.quarter_input}") @RequestParam(name="quarter", required=false) String quarter) {
		return ResponseEntity.ok(squadReportsManagement.getSquadsAndReportStatus(domainId, quarter));	
	}
	
	@GetMapping(value = "/filtered", produces = JSON) @ApiOperation("${squad_reports.get_by_squad_description}")
	public ResponseEntity<EditSquadReportDO> getSquadReport(
			@ApiParam("${squad_reports.squad_input}") @RequestParam(name="squadId", required=true) Integer squadId) {
		return ResponseEntity.ok(squadReportsManagement.getCurrentQuarterSquadReport(squadId));
	}
	
	@GetMapping(value = "/{id}", produces = JSON) @ApiOperation("${squad_reports.get_by_id_description}")
	public ResponseEntity<EditSquadReportDO> getReport(
			@ApiParam("${squad_reports.get_input}") @PathVariable("id") int reportId) {
		return ResponseEntity.ok(squadReportsManagement.getReport(reportId));	
	}
	
	@PostMapping(value = "/add", produces = JSON) @ApiOperation("${squad_reports.add_description}")
	public ResponseEntity<DefaultResponse> saveNewReport(
			@ApiParam("${squad_reports.add_input}") @RequestBody EditSquadReportDO bean) {
		int createdReportId = squadReportsManagement.createNewReport(bean);
		if (createdReportId > 0) {
			return ResponseEntity.ok(new DefaultResponse("Created report with id: " + createdReportId));
		} else {
			return ResponseEntity.ok(new DefaultResponse("Problems encountered, report was not saved"));
		}
	}
	
	@PutMapping(value = "/save", produces = JSON) @ApiOperation("${squad_reports.save_description}")
	public ResponseEntity<DefaultResponse> updateReport(
			@ApiParam("${squad_reports.save_input}") @RequestBody EditSquadReportDO bean) {
		if (squadReportsManagement.updateReport(bean)) {
			return ResponseEntity.ok(new DefaultResponse("Updated report"));
		} else {
			return ResponseEntity.ok(new DefaultResponse("Problems encountered, report was not updated"));
		}
	}
	
	@PutMapping(value = "/status", produces = JSON) @ApiOperation("${squad_reports.update_status_description}")
	public ResponseEntity<DefaultResponse> updateReportStatus(
			@ApiParam("${squad_reports.status_input}") @RequestBody ReportStatusDO bean) {
		if (squadReportsManagement.updateReportStatus(bean)) {
			return ResponseEntity.ok(new DefaultResponse("Updated report status"));
		} else {
			return ResponseEntity.ok(new DefaultResponse("Problems encountered, report was not updated"));
		}
	}
}
