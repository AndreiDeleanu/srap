package com.ibm.srap.controllers;

import static com.ibm.srap.services.utils.Messages.JSON;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.srap.client_beans.DefaultResponse;
import com.ibm.srap.client_beans.ThresholdDO;
import com.ibm.srap.services.ThresholdService;
import com.ibm.srap.services.utils.SrapCallsHandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/thresholds")
@PropertySource("classpath:swagger.properties")
@Api(tags = "Thresholds")
public class ThresholdsController {

	@Autowired
	private ThresholdService thresholdService;
	@Autowired
	private SrapCallsHandler srapCallsHandler;

	@PostMapping(value = "/add", produces = JSON)
	@ApiOperation("${thresholds.add_description}")
	public ResponseEntity<DefaultResponse> createNewThreshold(
			@ApiParam("${thresholds.add_input}") @RequestBody ThresholdDO thresholdDO) {
		return srapCallsHandler.handleOperationResponse(thresholdService.createNewThreshold(thresholdDO));
	}

	@GetMapping(value = "/getThreshold", produces = JSON)
	@ApiOperation("${thresholds.getByDomainID_description}")
	public ResponseEntity<ThresholdDO> getThreshold(
			@ApiParam("${thresholds.getByDomainID_input}") @RequestParam(value = "domainId") Integer domainId) {
		return ResponseEntity.ok(thresholdService.getThresholdsByDomainIdForCurrentQuarterAndYear(domainId));
	}

	@PutMapping(value = "/update", produces = JSON)
	@ApiOperation("${thresholds.update_description}")
	public ResponseEntity<DefaultResponse> update(
			@ApiParam("${thresholds.update_input}") @RequestBody ThresholdDO thresholdDO) {
		return srapCallsHandler.handleOperationResponse(thresholdService.updateThreshold(thresholdDO));
	}

	@GetMapping(value = "/getSpecificThreshold", produces = JSON)
	@ApiOperation("${thresholds.getSpecificThreshold_description}")
	public ResponseEntity<ThresholdDO> getSpecificThreshold(
			@ApiParam("${thresholds.getSpecificThreshold_input}") @RequestParam(value = "domainId") Integer domainId,
			@RequestParam(value = "quarter") String quarter, @RequestParam(value = "year") String year) {
		return ResponseEntity.ok(thresholdService.getSpecificThresholdByDomainIdQuarterYear(domainId, quarter, year));
	}

	@GetMapping(value = "/getAllByDomainId", produces = JSON)
	@ApiOperation("${thresholds.getAllByDomainId_description}")
	public ResponseEntity<List<ThresholdDO>> getAllThresholdsByDomainId(
			@ApiParam("${thresholds.getAllByDomainId_input}") @RequestParam(value = "domainId") Integer domainId) {
		return ResponseEntity.ok(thresholdService.getAllThresholdsByDomainId(domainId));
	}
}
