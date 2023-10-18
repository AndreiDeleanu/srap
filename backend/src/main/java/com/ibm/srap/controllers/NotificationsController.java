package com.ibm.srap.controllers;

import static com.ibm.srap.services.utils.Messages.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.srap.client_beans.DefaultResponse;
import com.ibm.srap.client_beans.NotificationDO;
import com.ibm.srap.services.NotificationService;
import com.ibm.srap.services.utils.SrapCallsHandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController @RequestMapping("/notifications")
@PropertySource("classpath:swagger.properties")
@Api(tags = "Notifications")
public class NotificationsController {
	
	@Autowired private SrapCallsHandler srapCallsHandler;
	@Autowired private NotificationService notificationService;
	
	
	@PostMapping(value = "/send", produces = JSON) @ApiOperation("${notifications.send_description}")
	public ResponseEntity<DefaultResponse> send(@ApiParam("${notifications.send_input}") @RequestBody NotificationDO notification) {
		return srapCallsHandler.handleOperationResponse(notificationService.sendNotification(notification));
	}

}
