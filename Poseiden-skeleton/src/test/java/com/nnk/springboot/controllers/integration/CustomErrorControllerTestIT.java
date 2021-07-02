package com.nnk.springboot.controllers.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;


/**
 * Unit testing of a custom ErrorController with MockMvc is unfortunately not supported.
 * Spring Boot Team recommendation for tests to be sure that error handling is working correctly, is to use an embedded
 * container and test with WebTestClient, RestAssured, or TestRestTemplate.
 * https://stackoverflow.com/questions/52925700/cannot-properly-test-errorcontroller-spring-boot
 *
 * @author jerome
 *
 */

//Spring Boot autoconfigures an instance of the WebTestClient whenever you select WebEnvironment.RANDOM_PORT 
//or WebEnvironment.DEFINED_PORT for @SpringBootTest
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

class CustomErrorControllerTestIT {

	@Autowired
	private WebTestClient webTestClient;
	
	@Test
	void GET_error_shouldSucceed_404() {
	  this.webTestClient
	    .get()
	    .uri("/user/pageNotExist") //user/** is allowed for all in Security config
	    //.header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
	    .exchange()
	    .expectStatus().is4xxClientError();
	}
	

}
