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
import com.ibm.srap.client_beans.OperationResult;
import com.ibm.srap.client_beans.SquadDO;
import com.ibm.srap.client_beans.SquadUpdateDO;
import com.ibm.srap.services.SquadService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController @RequestMapping(value="/squads")
@PropertySource("classpath:swagger.properties")
@Api(tags = "Squads")
public class SquadsController {
	
	@Autowired private SquadService squadsManagement;

	
	@GetMapping(produces = JSON) @ApiOperation("${squads.get_description}")
	public ResponseEntity<List<SquadDO>> getAllForDomain(@ApiParam("${squads.domain_input}") @RequestParam("domain") Integer domainId) {
		return ResponseEntity.ok(squadsManagement.getSquadsByDomain(domainId));
	}
	
	@PostMapping(value = "/add", produces = JSON) @ApiOperation("${squads.add_description}")
	public ResponseEntity<DefaultResponse> saveNew(@ApiParam("${squads.add_input}") @RequestBody SquadDO squadBean) {
		if (squadsManagement.createNewSquad(squadBean)) {
			return ResponseEntity.ok(new DefaultResponse("Created squad: " + squadBean.getName()));
		} else {
			return ResponseEntity.ok(new DefaultResponse("Problems encountered, squad was not saved"));
		}
	}
	
	@PutMapping(value = "/update", produces = JSON) @ApiOperation("${squads.update_description}")
	public ResponseEntity<DefaultResponse> updateSquad(@ApiParam("${squads.update_input}") @RequestBody SquadUpdateDO updateBean) {
		OperationResult result = squadsManagement.updateSquad(updateBean);
		if (result.isSuccessful()) {
			return ResponseEntity.ok(new DefaultResponse(result.getMessage()));
		} else {
			return ResponseEntity.badRequest().body(new DefaultResponse(result.getMessage()));
		}
	}

}