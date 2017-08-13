package com.n26.challenge.controller.utils;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


/**
 * Configures Springfox to create Swagger 2 API documentation.
 *
 * @see <a href="http://www.baeldung.com/swagger-2-documentation-for-spring-rest-api"> Setting up
 *      Swagger 2 with a Spring REST API</a>
 *
 */

@Configuration
public class SwaggerConfig {


  ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("N26 Challenge")
        .description(
            "#### Change History | Date | Name | Changes | Status |Version | |------|------|---------|--------|--------|   | 12.08.2017 | Taieb BEN DAI | Initial creation |**Finished**|**-**| 1.0.0.0  ")
        .license("").licenseUrl("http://unlicense.org").termsOfServiceUrl("").version("0.1")
        .build();
  }

  /**
   * Generated custom implementation by Swagger.
   *
   * @return a Docket, which is a swagger-specific interface builder
   */
  @Bean
  public Docket customImplementation() {
    return new Docket(DocumentationType.SWAGGER_2).groupName("N26 Challenge").apiInfo(apiInfo())
        .select().paths(regex("/api/v1.*")).build();

  }

}
