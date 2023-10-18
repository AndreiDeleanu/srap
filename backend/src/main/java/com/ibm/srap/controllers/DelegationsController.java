package com.ibm.srap.controllers;

import static com.ibm.srap.services.utils.Messages.JSON;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ibm.srap.client_beans.DefaultResponse;
import com.ibm.srap.client_beans.DelegationDO;
import com.ibm.srap.services.DelegationService;
import com.ibm.srap.services.utils.SrapCallsHandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller @RequestMapping(value="/delegations")
@PropertySource("classpath:swagger.properties")
@Api(tags = "Delegations")
public class DelegationsController {
	
	@Autowired private DelegationService delegationService;
	@Autowired private SrapCallsHandler srapCallsHandler;
	
	
	@GetMapping(produces = JSON) @ApiOperation("${delegations.get_description}")
	public ResponseEntity<List<DelegationDO>> get(@ApiParam("${delegations.get_input}") @RequestParam("email") String input) {
		return ResponseEntity.ok(delegationService.getDelegations(input));
	}
	
	@PostMapping(value = "/add", produces = JSON) @ApiOperation("${delegations.add_description}")
	public ResponseEntity<DefaultResponse> add(@ApiParam("${delegations.add_input}") @RequestBody DelegationDO input) {
		return srapCallsHandler.handleOperationResponse(delegationService.createDelegation(input));
	}
	
	@PutMapping(value = "/update", produces = JSON) @ApiOperation("${delegations.update_description}")
	public ResponseEntity<DefaultResponse> update(@ApiParam("${delegations.update_input}") @RequestBody DelegationDO input) {
		return srapCallsHandler.handleOperationResponse(delegationService.updateDelegation(input));
	}
	
	@DeleteMapping(value = "/delete", produces = JSON) @ApiOperation("${delegations.delete_description}")
	public ResponseEntity<DefaultResponse> delete(@ApiParam("${delegations.delete_input}") @RequestBody DelegationDO input) {
		return srapCallsHandler.handleOperationResponse(delegationService.deleteDelegation(input));
	}

}
