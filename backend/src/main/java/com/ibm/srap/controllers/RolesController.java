package com.ibm.srap.controllers;

import static com.ibm.srap.services.utils.Messages.JSON;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.srap.client_beans.IdNameDO;
import com.ibm.srap.client_beans.DefaultResponse;
import com.ibm.srap.services.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController @RequestMapping(value="/roles")
@PropertySource("classpath:swagger.properties")
@Api(tags = "Roles")
public class RolesController {
	
	@Autowired private RoleService rolesManagement;
	
	
	@GetMapping(produces = JSON) @ApiOperation("${roles.get_description}")
	public ResponseEntity<List<IdNameDO>> getAllOrByName(
			@ApiParam("${roles.get_input}") @RequestParam(value="name", required=false) String name) {
		if (name == null) {
			return ResponseEntity.ok(rolesManagement.getAllRoles());
		} else {
			return ResponseEntity.ok(rolesManagement.getRolesByName(name));
		}
	}
	
	@PostMapping(value = "/add", produces = JSON) @ApiOperation("${roles.add_description}")
	public ResponseEntity<DefaultResponse> saveNew(@ApiParam("${roles.add_input}") @RequestBody IdNameDO bean) {
		if (rolesManagement.createNewRole(bean)) {
			return ResponseEntity.ok(new DefaultResponse("Created role: " + bean.getName()));
		} else {
			return ResponseEntity.ok(new DefaultResponse("Problems encountered, role was not saved"));
		}
	}

}
