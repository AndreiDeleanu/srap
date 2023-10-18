package com.ibm.srap.controllers;

import static com.ibm.srap.services.utils.Messages.JSON;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.srap.client_beans.DefaultResponse;
import com.ibm.srap.client_beans.DomainReportDO;
import com.ibm.srap.client_beans.OperationResult;
import com.ibm.srap.services.DomainReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController @RequestMapping("/domains/reports")
@PropertySource("classpath:swagger.properties")
@Api(tags = "Domain Reports")
public class DomainReportsController {
	
	@Autowired private DomainReportService domainReportsManagement;
	
	
	@GetMapping(produces = JSON) @ApiOperation("${domain_reports.get_description}")
	public ResponseEntity<List<DomainReportDO>> getDomainReports(
			@ApiParam("${domain_reports.domain_input}") @RequestParam(name="domainId", required=true) Integer domainId,
			@ApiParam("${domain_reports.quarter_input}") @RequestParam(name="quarter", required=false) String quarter) {
		return ResponseEntity.ok(domainReportsManagement.getDomainReports(domainId, quarter));
	}
	
	@PutMapping(value = "/save", produces = JSON) @ApiOperation("${domain_reports.save_description}")
	public ResponseEntity<DefaultResponse> saveDomainReport(@ApiParam("${domain_reports.save_input}") @RequestBody DomainReportDO bean)  {
		OperationResult response = domainReportsManagement.saveReport(bean);
		if (response.isSuccessful()) {
			return ResponseEntity.ok(new DefaultResponse(response.getMessage()));
		} else {
			return ResponseEntity.badRequest().body(new DefaultResponse(response.getMessage()));
		}
	}
	
}
