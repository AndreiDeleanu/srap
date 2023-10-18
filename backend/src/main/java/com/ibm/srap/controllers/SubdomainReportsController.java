package com.ibm.srap.controllers;

import static com.ibm.srap.services.utils.Messages.JSON;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.srap.client_beans.DefaultResponse;
import com.ibm.srap.client_beans.OperationResult;
import com.ibm.srap.client_beans.ReportStatusDO;
import com.ibm.srap.client_beans.SubdomainReportDO;
import com.ibm.srap.client_beans.SubdomainReportSummaryDO;
import com.ibm.srap.services.SubdomainReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController @RequestMapping(value="/subdomains/reports")
@PropertySource("classpath:swagger.properties")
@Api(tags = "Subdomain Reports")
public class SubdomainReportsController {
	
	@Autowired private SubdomainReportService subdomainReportsManagement;
	
	
	@GetMapping(produces = JSON) @ApiOperation("${subdomain_reports.get_description}")
	public ResponseEntity<List<SubdomainReportDO>> getSubdomainReports(
			@ApiParam("${subdomain_reports.subdomain_input}") @RequestParam("subdomainId") Integer subdomainId) {
		return ResponseEntity.ok(subdomainReportsManagement.getSubdomainReportsFor(subdomainId));
	}
	
	@GetMapping(value = "/filtered", produces = JSON) @ApiOperation("${subdomain_reports.get_summary_description}")
	public ResponseEntity<List<SubdomainReportSummaryDO>> getSubdomainReportsByQuarter(
			@ApiParam("${subdomain_reports.subdomain_input}") @RequestParam(name="subdomainId", required=false) Integer subdomainId,
			@ApiParam("${subdomain_reports.quarter_input}") @RequestParam(name="quarter", required=false) String quarter, 
			@ApiParam("${subdomain_reports.domain_input}") @RequestParam(name="domainId", required=false) Integer domainId) {
		if (subdomainId != null) return ResponseEntity.ok(subdomainReportsManagement.getReportsBySubdomain(subdomainId, quarter));
		if (domainId != null) return ResponseEntity.ok(subdomainReportsManagement.getReportsByDomain(domainId, quarter));
		return ResponseEntity.badRequest().body(new ArrayList<>());
	}
	
	@GetMapping(value = "/{id}", produces = JSON) @ApiOperation("${subdomain_reports.get_by_id_description}")
	public ResponseEntity<SubdomainReportDO> getReport(
			@ApiParam("${subdomain_reports.get_input}") @PathVariable("id") int reportId) {
		return ResponseEntity.ok(subdomainReportsManagement.getReport(reportId));	
	}
	
	@GetMapping(value = "/current", produces = JSON) @ApiOperation("${subdomain_reports.get_current_description}")
	public ResponseEntity<List<SubdomainReportDO>> getCurrentSubdomainReports(
			@ApiParam("${subdomain_reports.subdomain_input}") @RequestParam("subdomainId") Integer subdomainId) {
		return ResponseEntity.ok(subdomainReportsManagement.getCurrentQuarterReportFor(subdomainId));
	}
	
	@PutMapping(value = "/save", produces = JSON) @ApiOperation("${subdomain_reports.save_description}")
	public ResponseEntity<DefaultResponse> saveSubdomainReport(
			@ApiParam("${subdomain_reports.save_input}") @RequestBody SubdomainReportDO bean)  {
		OperationResult response = subdomainReportsManagement.saveReport(bean);
		if (response.isSuccessful()) {
			return ResponseEntity.ok(new DefaultResponse(response.getMessage()));
		} else {
			return ResponseEntity.badRequest().body(new DefaultResponse(response.getMessage()));
		}
	}
	
	@PutMapping(value = "/status", produces = JSON) @ApiOperation("${subdomain_reports.update_status_description}")
	public ResponseEntity<DefaultResponse> updateReportStatus(
			@ApiParam("${subdomain_reports.status_input}") @RequestBody ReportStatusDO bean) {
		if (subdomainReportsManagement.updateReportStatus(bean)) {
			return ResponseEntity.ok(new DefaultResponse("Updated report status"));
		} else {
			return ResponseEntity.ok(new DefaultResponse("Problems encountered, report was not updated"));
		}
	}

}
