package com.nnk.springboot.controllers.integration;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nnk.springboot.testconfig.SpringWebIntegrationTestConfig;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(SpringWebIntegrationTestConfig.class)
class CustomErrorControllerTestIT{

	Logger logger = LoggerFactory.getLogger(CustomErrorControllerTestIT.class);
	
	@Autowired
    private MockMvc mvc;
	
	@Autowired
    private WebApplicationContext context;
	
	//When using @SpringBootTest annotation to test controllers with Spring Security, it's necessary to explicitly
	//configure the filter chain when setting up MockMvc:
	@BeforeEach
	public void setup() {
	     mvc = MockMvcBuilders
	    .webAppContextSetup(context)
	    .apply(springSecurity())
	    .build();
	}
	
	
	@Test
	void GET_userPageNotExist_shouldError_404NotFound() throws Exception {
		 mvc.perform(get("/user/pageNotExist")) //user/** is allowed for all in Security config
				 .andExpect(status().isNotFound())
	    ;
	
	}
	
	@WithMockUser // default authority USER
	@Test
	void GET_adminHome_shouldError_403Forbidden() throws Exception {
		mvc.perform(get("/admin/home"))
		 .andExpect(status().isForbidden())
		 ;

	}
	
	

}
