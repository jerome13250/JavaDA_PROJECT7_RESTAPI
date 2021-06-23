package com.nnk.springboot.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
	
	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void POST_bidListValidate_shouldSucceedWithRedirection() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(post("/bidList/validate")
				.param("account", "accountOfBid")
				.param("type", "typeOfBid")
				.param("bidQuantity", "1000")
				.with(csrf())
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/bidList/list"))
		;
		
		ArgumentCaptor<BidList> bidListCaptor = ArgumentCaptor.forClass(BidList.class);
		verify(bidListService).save(bidListCaptor.capture());
		BidList savedBidList = bidListCaptor.getValue();
		assertEquals("accountOfBid",savedBidList.getAccount());
		assertEquals("typeOfBid",savedBidList.getType());
		assertEquals(1000d,savedBidList.getBidQuantity());
		
	}

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void POST_bidListValidate_formValidationFailed_NoData() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(post("/bidList/validate")
				//.param("account", "accountOfBid")
				//.param("type", "typeOfBid")
				//.param("bidQuantity", "1000")
				.with(csrf())
				)
		.andExpect(status().isOk()) //return to validate page to display error
		.andExpect(view().name("/bidList/add"))
		.andExpect(model().size(1))
		.andExpect(model().attributeErrorCount("bidList", 3))
		.andExpect(model().attributeHasFieldErrorCode("bidList", "account", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("bidList", "type", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("bidList", "bidQuantity", "NotNull"))
		;
		
		//BidList must not be saved
		verify(bidListService,never()).save(any(BidList.class));
		
	}
	
	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void POST_bidListValidate_formValidationFailed_BlankAndInvalidDigits() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(post("/bidList/validate")
				.param("account", "")
				.param("type", "")
				.param("bidQuantity", "100000000000000000000000000000000")
				.with(csrf())
				)
		.andExpect(status().isOk()) //return to validate page to display error
		.andExpect(view().name("/bidList/add"))
		.andExpect(model().size(1))
		.andExpect(model().attributeErrorCount("bidList", 3))
		.andExpect(model().attributeHasFieldErrorCode("bidList", "account", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("bidList", "type", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("bidList", "bidQuantity", "Digits"))
		;
		
		//BidList must not be saved
		verify(bidListService,never()).save(any(BidList.class));
		
	}
	

}
