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
import org.springframework.web.bind.annotation.RestController;

import com.ibm.srap.client_beans.DefaultResponse;
import com.ibm.srap.client_beans.DomainDO;
import com.ibm.srap.services.DomainService;
import com.ibm.srap.services.utils.SrapCallsHandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController @RequestMapping("/domains")
@PropertySource("classpath:swagger.properties")
@Api(tags = "Domains")
public class DomainsController {
	
	@Autowired private DomainService domainsManagement;
	@Autowired private SrapCallsHandler srapCallsHandler;
	
	
	@GetMapping(produces = JSON) @ApiOperation("${domains.get_all_description}")
	public ResponseEntity<List<DomainDO>> getAllDomains() {
		return ResponseEntity.ok(domainsManagement.getAllDomains());
	}
	
	@GetMapping(value = "/{id}", produces = JSON) @ApiOperation("${domains.get_description}")
	public ResponseEntity<DomainDO> getDomain(@ApiParam("${domains.get_input}") @PathVariable("id") Integer id) {
		return ResponseEntity.ok(domainsManagement.getDomainBy(id));
	}
	
	@PostMapping(value = "/add", produces = JSON) @ApiOperation("${domains.add_description}")
	public ResponseEntity<DefaultResponse> saveNew(@ApiParam("${domains.add_input}") @RequestBody DomainDO domainBean) {
		return srapCallsHandler.handleOperationResponse(domainsManagement.createNewDomain(domainBean));
	}
	
	@PutMapping(value = "/save", produces = JSON) @ApiOperation("${domains.save_description}")
	public ResponseEntity<DefaultResponse> updateDomain(@ApiParam("${domains.save_input}") @RequestBody DomainDO bean) {
		return srapCallsHandler.handleOperationResponse(domainsManagement.updateDomain(bean));
	}
	
}
