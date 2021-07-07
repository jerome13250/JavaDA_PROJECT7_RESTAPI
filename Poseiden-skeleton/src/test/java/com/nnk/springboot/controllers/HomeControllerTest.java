package com.nnk.springboot.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.nnk.springboot.testconfig.SpringWebTestConfig;

//@WebMvcTest tells Spring Boot to instantiate only the web layer and not the entire context
@WebMvcTest(controllers = HomeController.class) 
//Need to create a UserDetailsService in SpringSecurityWebTestConfig.class because @Service are not loaded by @WebMvcTest :
@Import(SpringWebTestConfig.class)

class HomeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void GET_appLogin_shouldSucceed() throws Exception {
		mockMvc.perform(get("/"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("home"))
		;
	}
	
	//@WithMockUser : If we don't specify the username or role, the default username is user and default role is ROLE_USER.
	//Note that it isn't necessary to add the ROLE_ prefix on "role", Spring Security will add that prefix automatically.
	//If we don't want to have that prefix, we can consider using "authority" instead of "role": 
	//https://www.baeldung.com/spring-security-method-security
	@WithMockUser(authorities="ADMIN")
	@Test
	void GET_adminHome_shouldSucceed() throws Exception {
		mockMvc.perform(get("/admin/home"))
		.andExpect(status().is3xxRedirection()) 
		.andExpect(view().name("redirect:/bidList/list"))
		;
	}
	
	@WithMockUser(roles="USER")
	@Test
	void GET_adminHome_shouldFailError403() throws Exception {
		mockMvc.perform(get("/admin/home"))
		.andExpect(status().isForbidden()) //403 forbidden
		;
	}
	
	@Test
	void GET_adminHome_shouldBeRedirectedToLogin() throws Exception {
		mockMvc.perform(get("/admin/home"))
		.andExpect(status().is3xxRedirection()) //redirected to login
		;
	}
	
}
