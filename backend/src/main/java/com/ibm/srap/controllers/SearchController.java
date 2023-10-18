package com.ibm.srap.controllers;

import static com.ibm.srap.services.utils.Messages.JSON;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.srap.client_beans.SearchResultUserDO;
import com.ibm.srap.services.BluepagesApiCallerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController @RequestMapping("/search")
@PropertySource("classpath:swagger.properties")
@Api(tags = "Search")
public class SearchController {
	
	@Autowired private BluepagesApiCallerService bluepages;
	
	
	@GetMapping(produces = JSON) @ApiOperation("${search.description}")
	public ResponseEntity<List<SearchResultUserDO>> searchUser(
			@ApiParam("${search.input}") @RequestParam("user") String input) {
		return ResponseEntity.ok(bluepages.findUsers(input));
	}

}
