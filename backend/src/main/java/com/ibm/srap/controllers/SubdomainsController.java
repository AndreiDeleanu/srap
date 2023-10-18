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
import com.ibm.srap.client_beans.SubdomainDO;
import com.ibm.srap.services.SubdomainService;
import com.ibm.srap.services.utils.SrapUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController @RequestMapping(value="/subdomains")
@PropertySource("classpath:swagger.properties")
@Api(tags = "Subdomains")
public class SubdomainsController {
	
	@Autowired private SubdomainService subdomainsManagement;
	
	
	@GetMapping(produces = JSON) @ApiOperation("${subdomains.get_description}")
	public ResponseEntity<List<SubdomainDO>> getAllForDomain(
			@ApiParam("${subdomains.domain_input}") @RequestParam("domain") Integer domainId) {
		return ResponseEntity.ok(subdomainsManagement.getAllForDomain(domainId));
	}
	
	@PostMapping(value = "/add", produces = JSON) @ApiOperation("${subdomains.add_description}")
	public ResponseEntity<DefaultResponse> createDomain(
			@ApiParam("${subdomains.add_input}") @RequestBody SubdomainDO bean) {
		return SrapUtils.evaluateOperationResult(subdomainsManagement.createNewSubdomain(bean));
	}
	
	@PutMapping(value = "/save", produces = JSON) @ApiOperation("${subdomains.update_description}")
	public ResponseEntity<DefaultResponse> updateDomain(
			@ApiParam("${subdomains.update_input}") @RequestBody SubdomainDO bean) {
		return SrapUtils.evaluateOperationResult(subdomainsManagement.updateSubdomain(bean));
	}

}
