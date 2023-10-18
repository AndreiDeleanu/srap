package com.ibm.srap.controllers;

import static com.ibm.srap.services.utils.Messages.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.srap.client_beans.CalculatedValuesGraphDO;
import com.ibm.srap.client_beans.FLLRatingGraphDO;
import com.ibm.srap.client_beans.ProgressGraphDO;
import com.ibm.srap.services.SquadReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/graphs")
@PropertySource("classpath:swagger.properties")
@Api(tags = "Graphs")
public class GraphsController {

	@Autowired
	private SquadReportService squadReportService;

	@GetMapping(value = "/countFLLratings", produces = JSON)
	@ApiOperation("${graphs.get_fll_ratings_graph_description}")
	public ResponseEntity<FLLRatingGraphDO> countFLLratingsGraph(
			@ApiParam("${graphs.get_fll_ratings_graph_input}") @RequestParam("domainId") int domainId) {

		return ResponseEntity.ok(squadReportService.getFLLratingsGraph(domainId));

	}

	@GetMapping(value = "/countCalculatedRatings", produces = JSON)
	@ApiOperation("${graphs.get_calculated_ratings_description}")
	public ResponseEntity<CalculatedValuesGraphDO> countCalculatedRatingValuesGraph(
			@ApiParam("${graphs.get_calculated_ratings_input}") @RequestParam("domainId") int domainId) {

		return ResponseEntity.ok(squadReportService.getCalculatedRatingValuesGraph(domainId));

	}

	@GetMapping(value = "/countProgressGraph", produces = JSON)
	@ApiOperation("${graphs.get_progress_graph_description}")
	public ResponseEntity<ProgressGraphDO> countValuesProgressGraph(
			@ApiParam("${graphs.get_progress_graph_input}") @RequestParam("domainId") int domainId) {

		return ResponseEntity.ok(squadReportService.getProgressGraphValue(domainId));

	}

}
