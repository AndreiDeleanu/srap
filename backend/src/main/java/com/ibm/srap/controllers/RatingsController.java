package com.ibm.srap.controllers;

import static com.ibm.srap.services.utils.Messages.JSON;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.srap.client_beans.RatingDO;
import com.ibm.srap.services.RatingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController @RequestMapping(value="/ratings")
@PropertySource("classpath:swagger.properties")
@Api(tags = "Ratings")
public class RatingsController {
	
	@Autowired RatingService ratingsManagement;
	
	
	@GetMapping(produces = JSON) @ApiOperation("${ratings.get_description}")
	public ResponseEntity<List<RatingDO>> getAll() {
		return ResponseEntity.ok(ratingsManagement.getAllRatings());
	}

}
