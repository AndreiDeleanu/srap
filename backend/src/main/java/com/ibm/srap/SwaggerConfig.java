package com.ibm.srap;

import java.sql.Timestamp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@Profile("dev")
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.ibm.srap"))
				.paths(PathSelectors.any()).build().directModelSubstitute(Timestamp.class, Long.class)
				.tags(new Tag("Authentication",
						"Main entry point into the application, the call accesses Bluepages API to authenticate the user"),
						new Tag("Deadlines", "REST calls for deadlines management"),
						new Tag("Delegations", "REST calls for delegations management"),
						new Tag("Domains", "REST calls for domains management"),
						new Tag("Domain Reports", "REST calls for domain reports management"),
						new Tag("Notifications", "REST call for notification e-mailing"),
						new Tag("Processes", "REST calls for processes management"),
						new Tag("Ratings", "REST call for retrieving defined ratings"),
						new Tag("Roles", "REST calls for user role management"),
						new Tag("Search", "REST call for searching specific user in BluePages"),
						new Tag("Squads", "REST calls for squads management"),
						new Tag("Squad Reports", "REST calls for squad reports management"),
						new Tag("Subdomains", "REST calls for subdomains management"),
						new Tag("Subdomain Reports", "REST calls for subdomain reports management"),
						new Tag("Thresholds", "REST calls for thresholds management"))
				.apiInfo(metaData());
	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("SRAP Portal").description("\"Spring Boot REST API\"").version("1.0.0")
				.license("Apache License Version 2.0").licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
				.contact(
						new Contact("SRAP Application on Bluemix", "https://srap.w3ibm.mybluemix.net/srap/index.html#/", ""))
				.build();
	}

}
