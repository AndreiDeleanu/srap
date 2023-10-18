package com.ibm.srap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ibm.srap.services.utils.JwtFilter;

@SpringBootApplication
@EnableScheduling
public class SrapApplication {


	@Bean
	public JwtFilter authenticationTokenFilterBean() {
		return new JwtFilter();
	}
	
	@Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(authenticationTokenFilterBean());
        registrationBean.addUrlPatterns(
        		"/srap/processes/*",
        		"/srap/roles/*",
        		"/srap/users/*",
        		"/srap/squads/*",
        		"/srap/ratings/*",
        		"/srap/domains/*",
        		"/srap/delegations/*",
        		"/srap/search/*",
        		"/srap/notifications/*",
        		"/srap/deadlines/*"
        		);
        return registrationBean;
    }
	
    
	public static void main(String[] args) {
		SpringApplication.run(SrapApplication.class, args);
	}
}