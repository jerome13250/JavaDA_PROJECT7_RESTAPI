package com.nnk.springboot.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.testconfig.SpringWebTestConfig;

//@WebMvcTest tells Spring Boot to instantiate only the web layer and not the entire context
@WebMvcTest(controllers = BidListController.class) 
//Need to create a UserDetailsService in SpringSecurityWebTestConfig.class because @Service are not loaded by @WebMvcTest :
@Import(SpringWebTestConfig.class)

class BidListControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private BidListService bidListService;
	
	List<BidList> BidListList;
	
	@BeforeEach
	void initialize() {
		BidList bid1 = new BidList("Account1", "Type1", 1d);
		bid1.setBidListId(1);
		BidList bid2 = new BidList("Account2", "Type2", 2d);
		bid2.setBidListId(2);
		BidList bid3 = new BidList("Account3", "Type3", 3d);
		bid3.setBidListId(3);
		BidListList = new ArrayList<>();
		BidListList.add(bid1);
		BidListList.add(bid2);
		BidListList.add(bid3);		
		
	}
	
	@Test
	void givenNotLogged_shouldBeRedirectedToLogin() throws Exception {
		mockMvc.perform(get("/bidList/list"))
		.andExpect(status().isFound())
		;
	}

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void givenMockUser_shouldSucceedWith200() throws Exception {
		//ARRANGE:
		when(bidListService.findAll()).thenReturn(BidListList);
		//ACT+ASSERT:
		mockMvc.perform(get("/bidList/list"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("bidList/list"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("listofbidlist"))
		;
	}

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void GET_addBidForm_shouldSucceedWith200() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(get("/bidList/add"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("bidList/add"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("bidList"))
		;
	}


}
