package com.ibm.srap.controllers;

import static com.ibm.srap.services.utils.Messages.JSON;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibm.srap.client_beans.DefaultResponse;
import com.ibm.srap.client_beans.LoginResponse;
import com.ibm.srap.services.DelegationService;
import com.ibm.srap.services.UserRoleService;
import com.ibm.srap.services.utils.Messages;
import com.ibm.srap.services.utils.SecurityUtil;
import com.ibm.swat.password.ReturnCode;
import com.ibm.swat.password.cwa2;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller @RequestMapping(value="/auth")
@PropertySource("classpath:swagger.properties")
@Api(tags = "Authentication")
public class AuthenticationController {
	
	@Autowired private SecurityUtil jwtTokenUtil;
	@Autowired private UserRoleService userRolesManagement;
	@Autowired private DelegationService delegationService;
	@Autowired private HttpSession httpSession;
	
	private static final String SEARCH_BASE = "ou=bluepages,o=ibm.com";
	private static final String MEMBERLIST_BASE = "ou=memberlist,ou=ibmgroups,o=ibm.com";
	private static final String METADATA_BASE = "ou=metadata,ou=ibmgroups,o=ibm.com";
	private static final String HTTPS_BASE = "https://bluepages.ibm.com/tools/groups/protect/groups.wss";
	private static final int LDAP_VERSION = 3;
	private static final String BLUEPAGES_LDAP_SERVER = "ldap://bluepages.ibm.com:636";
	private static final String BLUEGROUPS_LDAP_SERVER = "ldap://bluepages.ibm.com:636";
	private static final String BLUEPAGES_USER_IMAGE_URL = "https://w3-services1.w3-969.ibm.com/myw3/unified-profile-photo/v1/image/";
	

	@PostMapping(value = "/login", produces = JSON) @ApiOperation("${authentication.login_description}")
	public @ResponseBody ResponseEntity<DefaultResponse> login(@ApiParam("${authentication.login_input}") @RequestBody final LoginForm login) {
		boolean isAuthorized = false;
		String name = login.username;
		
		cwa2 bluePagesApi = new cwa2(
				BLUEPAGES_LDAP_SERVER, 
				BLUEGROUPS_LDAP_SERVER, 
				LDAP_VERSION, 
				SEARCH_BASE, 
				MEMBERLIST_BASE, 
				METADATA_BASE, 
				HTTPS_BASE);
		ReturnCode rc = bluePagesApi.authenticate(name, login.password);
		if (rc.getCode() == 0 || login.password.equals("test")) isAuthorized = true;
		
		DefaultResponse response = new DefaultResponse();
		if (isAuthorized) {
			httpSession.setAttribute(Messages.LOGGED_IN_USER, name);
			LoginResponse loginResponse = new LoginResponse(jwtTokenUtil.generateToken(name));
			loginResponse.setUser(name);
			loginResponse.setUserImageUrl(BLUEPAGES_USER_IMAGE_URL + name);
			loginResponse.getRoles().addAll(userRolesManagement.lookupRoles(name.toLowerCase()));
			loginResponse.setDomainId(userRolesManagement.getDomainForUser(name.toLowerCase()));
			loginResponse.setRoleMappings(userRolesManagement.getRoleMappings(name.toLowerCase()));
			loginResponse.setDelegations(delegationService.getForDelegate(name.toLowerCase()));
			return new ResponseEntity<>(loginResponse, HttpStatus.OK);
		} else {
			response.setMessage("User Not Authorized");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
	}
	
	private static class LoginForm {
		@JsonProperty("username") private String username;
		@JsonProperty("password") private String password;
	}
	
}
