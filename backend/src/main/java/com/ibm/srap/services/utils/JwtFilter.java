package com.ibm.srap.services.utils;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.GenericFilterBean;


public class JwtFilter extends GenericFilterBean {
	
    @Autowired 
    private SecurityUtil jwtTokenUtil;
    
    @Value("${jwt.header}")
    private String tokenHeader;
    

    /**
     *  JWT token authorization 
     */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authToken = httpRequest.getHeader(tokenHeader);
        
        if (authToken == null || !authToken.startsWith("Bearer ")) {
        	if (response instanceof HttpServletResponse) {
				HttpServletResponse httpServletResponse = (HttpServletResponse) response;
				httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "jwt token missing");
			}
        } else {
        	authToken = authToken.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(authToken);

            if (username == null || !jwtTokenUtil.validateToken(authToken)) {
            	HttpServletResponse httpServletResponse = (HttpServletResponse) response;
				httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, 
						"jwt token is invalid or user doesn't meet access requirements");
            } else {
            	request.setAttribute("cnum", jwtTokenUtil.getCnumFromToken(authToken));
            	request.setAttribute("manager", jwtTokenUtil.getManagerFromToken(authToken));
            	chain.doFilter(request, response);
            }
        }
	}

}

