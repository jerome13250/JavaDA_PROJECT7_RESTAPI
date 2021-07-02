package com.nnk.springboot.controllers;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.testconfig.SpringWebTestConfig;

//@WebMvcTest tells Spring Boot to instantiate only the web layer and not the entire context
@WebMvcTest(controllers = LoginController.class) 
//Need to create a UserDetailsService in SpringSecurityWebTestConfig.class because @Service are not loaded by @WebMvcTest :
@Import(SpringWebTestConfig.class)

class LoginControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UserRepository userRepository;
	
	@Test
	void GET_appLogin_shouldBeRedirectedToLogin() throws Exception {
		mockMvc.perform(get("/app/login"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/login"))
		;
	}
	
	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void GET_appSecure_shouldSucceedWith200() throws Exception {
		mockMvc.perform(get("/app/secure/article-details"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("user/list"))
		;
	}
	

	@Test
	void GET_appSecure_NotIdentified_shouldBeRedirected() throws Exception {
		mockMvc.perform(get("/app/secure/article-details"))
		.andExpect(status().isFound()) //redirected to login page
		;
	}
	

}
