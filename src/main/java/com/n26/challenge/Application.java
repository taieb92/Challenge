package com.n26.challenge;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Main entry point to the application.
 *
 */
@SpringBootApplication
@EnableSwagger2
@EnableAutoConfiguration
public class Application extends SpringBootServletInitializer {

  /**
   * Main method, entry point to the application.
   *
   * @param args default
   * @throws Exception default
   */
  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(Application.class);
  }



}
