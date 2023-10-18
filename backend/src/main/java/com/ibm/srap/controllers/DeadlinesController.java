package com.ibm.srap.controllers;

import static com.ibm.srap.services.utils.Messages.JSON;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ibm.srap.client_beans.DeadlineDO;
import com.ibm.srap.client_beans.DefaultResponse;
import com.ibm.srap.services.DeadlineService;
import com.ibm.srap.services.utils.SrapCallsHandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller @RequestMapping(value = "/deadlines")
@PropertySource("classpath:swagger.properties")
@Api(tags = "Deadlines")
public class DeadlinesController {
	
	@Autowired private SrapCallsHandler srapCallsHandler;
	@Autowired private DeadlineService deadlineService;
	
	
	@GetMapping(produces = JSON) @ApiOperation("${deadlines.get_description}")
	public ResponseEntity<List<DeadlineDO>> get(@ApiParam("${deadlines.get_input}") @RequestParam("domainId") Integer input) {
		return ResponseEntity.ok(deadlineService.getDeadlines(input));
	}
	
	@PostMapping(value = "/add", produces = JSON) @ApiOperation("${deadlines.add_description}")
	public ResponseEntity<DefaultResponse> add(@ApiParam("${deadlines.add_input}") @RequestBody DeadlineDO input) {
		return srapCallsHandler.handleOperationResponse(deadlineService.createDeadline(input));
	}
	
	@PutMapping(value = "/update", produces = JSON) @ApiOperation("${deadlines.update_description}")
	public ResponseEntity<DefaultResponse> update(@ApiParam("${deadlines.update_input}") @RequestBody DeadlineDO input) {
		return srapCallsHandler.handleOperationResponse(deadlineService.updateDeadline(input));
	}
	
}
