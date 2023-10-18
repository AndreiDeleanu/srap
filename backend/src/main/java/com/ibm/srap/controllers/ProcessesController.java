package com.ibm.srap.controllers;

import static com.ibm.srap.services.utils.Messages.JSON;
import static com.ibm.srap.services.utils.Messages.EXCEL;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
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
import com.ibm.srap.client_beans.ProcessDO;
import com.ibm.srap.client_beans.ProcessDashboardDO;
import com.ibm.srap.client_beans.SearchFilterParamsDO;
import com.ibm.srap.services.ProcessService;
import com.ibm.srap.services.utils.SrapCallsHandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/processes")
@PropertySource("classpath:swagger.properties")
@Api(tags = "Processes")
public class ProcessesController {

	@Autowired
	private ProcessService processManagement;
	@Autowired
	private SrapCallsHandler srapCallsHandler;

	@GetMapping(produces = JSON)
	@ApiOperation("${processes.get_by_domain_description}")
	public ResponseEntity<List<ProcessDO>> getDomainProcesses(
			@ApiParam("${processes.get_by_domain_input}") @RequestParam(value = "domainId") Integer domainId) {
		return ResponseEntity.ok(processManagement.getDomainProcesses(domainId));
	}

	@GetMapping(value = "/{id}", produces = JSON)
	@ApiOperation("${processes.get_by_id_description}")
	public ResponseEntity<ProcessDO> getById(@ApiParam("${processes.get_by_id_input}") @PathVariable("id") int id) {
		return ResponseEntity.ok(processManagement.getProcessById(id));
	}

	@PostMapping(value = "/add", produces = JSON)
	@ApiOperation("${processes.add_description}")
	public ResponseEntity<DefaultResponse> saveNew(@ApiParam("${processes.add_input}") @RequestBody ProcessDO processBean) {
		if (processManagement.createNewProcess(processBean)) {
			return ResponseEntity.ok(new DefaultResponse("Created process: " + processBean.getName()));
		} else {
			return ResponseEntity.ok(new DefaultResponse("Problems encountered, process was not saved"));
		}
	}

	@PutMapping(value = "/update", produces = JSON)
	@ApiOperation("${processes.save_description}")
	public ResponseEntity<DefaultResponse> update(@ApiParam("${processes.save_input}") @RequestBody ProcessDO processBean) {
		return srapCallsHandler.handleOperationResponse(processManagement.updateProcess(processBean));
	}

	@GetMapping(value = "/getDashboard", produces = JSON)
	@ApiOperation("${processes.get_dashboard_description}")
	public ResponseEntity<List<ProcessDashboardDO>> getProcessesForDashboard(
			@ApiParam("${processes.get_dashboard_input}") @RequestParam(value = "domainId") Integer domainId) {
		return ResponseEntity.ok(processManagement.getProcessesByDomainForCurrentQuarter(domainId));
	}

	@PostMapping(value = "/export", produces = EXCEL)
	@ApiOperation("${processes.export_description}")
	public ResponseEntity<InputStreamResource> exportProcessesFiltered(
			@ApiParam("${processes.export_input}") @RequestBody SearchFilterParamsDO searchFilterParamsDO) {

		XSSFWorkbook workbook;
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			workbook = processManagement.exportXLSX(searchFilterParamsDO);

			workbook.write(out);
		} catch (IOException e) {
			System.out.println(e.getMessage().toString());
			e.printStackTrace();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=processesFiltered.xlsx");

		return ResponseEntity.ok().headers(headers)
				.body(new InputStreamResource(new ByteArrayInputStream(out.toByteArray())));
	}

}