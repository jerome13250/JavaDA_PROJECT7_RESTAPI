package com.nnk.springboot.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.testconfig.OAuthUtils;
import com.nnk.springboot.testconfig.SpringWebUnitTestConfig;


//@WebMvcTest tells Spring Boot to instantiate only the web layer and not the entire context
@WebMvcTest(controllers = BidListController.class) 
//We create SpringSecurityWebTestConfig.class because @Service are not loaded by @WebMvcTest
//This way we create a UserDetailsService, a mock CustomOAuth2UserService and a mock ClientRegistrationRepository
@Import(SpringWebUnitTestConfig.class)

class BidListControllerTest {

	Logger logger = LoggerFactory.getLogger(BidListControllerTest.class);
	
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private BidListService bidListService;
	
	BidList bid1;
	List<BidList> BidListList;
	
	private OAuth2User oAuth2User;
	
	@BeforeEach
	void initialize() {
		bid1 = new BidList("Account1", "Type1", 1d);
		bid1.setBidListId(1);
		BidList bid2 = new BidList("Account2", "Type2", 2d);
		bid2.setBidListId(2);
		BidList bid3 = new BidList("Account3", "Type3", 3d);
		bid3.setBidListId(3);
		BidListList = new ArrayList<>();
		BidListList.add(bid1);
		BidListList.add(bid2);
		BidListList.add(bid3);		
		
		oAuth2User = OAuthUtils.createOAuth2User(
                "Jerome L", "jerome13250@example.com");
		
	}
	
	@Test
	void givenNotLogged_shouldBeRedirectedToLogin() throws Exception {
		//ACT+ASSERT
		mockMvc.perform(get("/bidList/list"))
		.andExpect(status().isFound()) //FOUND: status=302
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
	
	/**
	 * This test is for oauth2 user test.
	 * @throws Exception
	 */
	@Test
	void givenOauth2User_shouldSucceedWith200() throws Exception {
		//ARRANGE:
		when(bidListService.findAll()).thenReturn(BidListList);
		//ACT+ASSERT:
		mockMvc.perform(get("/bidList/list")
				.with(oauth2Login().oauth2User(oAuth2User)))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("bidList/list"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("listofbidlist"))
		.andExpect(content().string(containsString("Jerome L")));
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
	

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void GET_showUpdateForm_shouldSucceedWith200() throws Exception {
		//ARRANGE:
		when(bidListService.findById(1)).thenReturn(Optional.of(bid1));
		
		//ACT+ASSERT:
		mockMvc.perform(get("/bidList/update/1"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("bidList/update"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("bidList"))
		;
	}
	
	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void GET_showUpdateForm_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(bidListService.findById(1)).thenReturn(Optional.empty());
		
		//ACT+ASSERT:
		mockMvc.perform(get("/bidList/update/1"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("error"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("errorMsg"))
		;
	}
	
	@WithMockUser
	@Test
	void POST_bidListUpdate_shouldSucceedWithRedirection() throws Exception {
		//ARRANGE:
		when(bidListService.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT+ASSERT:
		mockMvc.perform(post("/bidList/update/1")
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
		assertEquals(1,savedBidList.getBidListId());
		assertEquals("accountOfBid",savedBidList.getAccount());
		assertEquals("typeOfBid",savedBidList.getType());
		assertEquals(1000d,savedBidList.getBidQuantity());
	}
	
	@WithMockUser
	@Test
	void POST_bidListUpdate_IdDoesNotExist_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(bidListService.existsById(1)).thenReturn(Boolean.FALSE);
		
		//ACT+ASSERT:
		mockMvc.perform(post("/bidList/update/1")
				.param("account", "accountOfBid")
				.param("type", "typeOfBid")
				.param("bidQuantity", "1000")
				.with(csrf())
				)
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("error"))
		.andExpect(model().attributeExists("errorMsg"))
		;
	}
	
	@WithMockUser
	@Test
	void POST_bidListUpdate_FormValidationFail_NoDAta_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(bidListService.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT+ASSERT:
		mockMvc.perform(post("/bidList/update/1")
				//.param("account", "accountOfBid")
				//.param("type", "typeOfBid")
				//.param("bidQuantity", "1000")
				.with(csrf())
				)
		.andExpect(status().isOk()) //return to validate page to display error
		.andExpect(view().name("bidList/update"))
		.andExpect(model().size(1))
		.andExpect(model().attributeErrorCount("bidList", 3))
		.andExpect(model().attributeHasFieldErrorCode("bidList", "account", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("bidList", "type", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("bidList", "bidQuantity", "NotNull"))
		;
	}
	
	
	@WithMockUser
	@Test
	void POST_bidListDelete_shouldSucceedWithRedirection() throws Exception {
		//ARRANGE:
		when(bidListService.existsById(1)).thenReturn(Boolean.TRUE);
				
		//ACT+ASSERT:
		mockMvc.perform(get("/bidList/delete/1")
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/bidList/list"))
		;
		
		verify(bidListService).deleteById(1);
		
	}
	
	@WithMockUser
	@Test
	void GET_bidListDelete_IdDoesNotExist_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(bidListService.existsById(1)).thenReturn(Boolean.FALSE);
		
		//ACT+ASSERT:
		mockMvc.perform(get("/bidList/delete/1")
					)
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("error"))
		.andExpect(model().attributeExists("errorMsg"))
		;
		
		//User must not be deleted
		verify(bidListService,never()).deleteById(1);
	}
	
}
