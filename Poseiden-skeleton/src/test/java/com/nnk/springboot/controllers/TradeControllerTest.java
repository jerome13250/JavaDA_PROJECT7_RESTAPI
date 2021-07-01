package com.nnk.springboot.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
import com.nnk.springboot.testconfig.SpringWebTestConfig;

//@WebMvcTest tells Spring Boot to instantiate only the web layer and not the entire context
@WebMvcTest(controllers = TradeController.class) 
//Need to create a UserDetailsService in SpringSecurityWebTestConfig.class because @Service are not loaded by @WebMvcTest :
@Import(SpringWebTestConfig.class)

class TradeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private TradeService tradeService;
	
	Trade trade1;
	List<Trade> TradeList;
	
	@BeforeEach
	void initialize() {
		new Trade();
		trade1 = new Trade("account1", "type1", 111.11);
		trade1.setTradeId(1);
		Trade trade2 = new Trade("account2", "type2", 222.22);
		trade2.setTradeId(2);
		Trade trade3 = new Trade("account3", "type3", 333.33);
		trade3.setTradeId(3);
		TradeList = new ArrayList<>();
		TradeList.add(trade1);
		TradeList.add(trade2);
		TradeList.add(trade3);		
		
	}
	
	@Test
	void givenNotLogged_shouldBeRedirectedToLogin() throws Exception {
		mockMvc.perform(get("/trade/list"))
		.andExpect(status().isFound())
		;
	}

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void givenMockUser_shouldSucceedWith200() throws Exception {
		//ARRANGE:
		when(tradeService.findAll()).thenReturn(TradeList);
		//ACT+ASSERT:
		mockMvc.perform(get("/trade/list"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("trade/list"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("listofTrade"))
		;
	}

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void GET_addBidForm_shouldSucceedWith200() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(get("/trade/add"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("trade/add"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("trade"))
		;
	}
	
	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void POST_tradeValidate_shouldSucceedWithRedirection() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(post("/trade/validate")
				.param("account", "account_999")
				.param("type", "type_999")
				.param("buyQuantity", "999.99")
				.with(csrf())
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/trade/list"))
		;
		
		ArgumentCaptor<Trade> tradeCaptor = ArgumentCaptor.forClass(Trade.class);
		verify(tradeService).save(tradeCaptor.capture());
		Trade savedTrade = tradeCaptor.getValue();
		assertEquals("account_999",savedTrade.getAccount());
		assertEquals("type_999",savedTrade.getType());
		assertEquals(999.99,savedTrade.getBuyQuantity());
		
	}

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void POST_tradeValidate_formValidationFailed_NoData() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(post("/trade/validate")
				//.param("account", "account_999")
				//.param("type", "type_999")
				//.param("buyQuantity", "999.99")
				.with(csrf())
				)
		.andExpect(status().isOk()) //return to validate page to display error
		.andExpect(view().name("/trade/add"))
		.andExpect(model().size(1))
		.andExpect(model().attributeErrorCount("trade", 3))
		.andExpect(model().attributeHasFieldErrorCode("trade", "account", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("trade", "type", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("trade", "buyQuantity", "NotNull"))
		;
		
		//Trade must not be saved
		verify(tradeService,never()).save(any(Trade.class));
		
	}
	

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void GET_showUpdateForm_shouldSucceedWith200() throws Exception {
		//ARRANGE:
		when(tradeService.findById(1)).thenReturn(Optional.of(trade1));
		
		//ACT+ASSERT:
		mockMvc.perform(get("/trade/update/1"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("trade/update"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("trade"))
		;
	}
	
	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void GET_showUpdateForm_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(tradeService.findById(1)).thenReturn(Optional.empty());
		
		//ACT+ASSERT:
		mockMvc.perform(get("/trade/update/1"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("error"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("errorMsg"))
		;
	}
	
	@WithMockUser
	@Test
	void POST_tradeUpdate_shouldSucceedWithRedirection() throws Exception {
		//ARRANGE:
		when(tradeService.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT+ASSERT:
		mockMvc.perform(post("/trade/update/1")
				.param("account", "account_999")
				.param("type", "type_999")
				.param("buyQuantity", "999.99")
				.with(csrf())
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/trade/list"))
		;
		
		ArgumentCaptor<Trade> tradeCaptor = ArgumentCaptor.forClass(Trade.class);
		verify(tradeService).save(tradeCaptor.capture());
		Trade savedTrade = tradeCaptor.getValue();
		assertEquals(1,savedTrade.getTradeId());
		assertEquals("account_999",savedTrade.getAccount());
		assertEquals("type_999",savedTrade.getType());
		assertEquals(999.99,savedTrade.getBuyQuantity());
	}
	
	@WithMockUser
	@Test
	void POST_tradeUpdate_IdDoesNotExist_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(tradeService.existsById(1)).thenReturn(Boolean.FALSE);
		
		//ACT+ASSERT:
		mockMvc.perform(post("/trade/update/1")
				.param("account", "account_999")
				.param("type", "type_999")
				.param("buyQuantity", "999.99")
				.with(csrf())
				)
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("error"))
		.andExpect(model().attributeExists("errorMsg"))
		;
	}
	
	@WithMockUser
	@Test
	void POST_tradeUpdate_FormValidationFail_NoDAta_shouldReturnToUpdatePage() throws Exception {
		//ARRANGE:
		when(tradeService.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT+ASSERT:
		mockMvc.perform(post("/trade/update/1")
				//.param("account", "account_999")
				//.param("type", "type_999")
				//.param("buyQuantity", "999.99")
				.with(csrf())
				)
		.andExpect(status().isOk()) //return to validate page to display error
		.andExpect(view().name("trade/update"))
		.andExpect(model().size(1))
		.andExpect(model().attributeErrorCount("trade", 3))
		.andExpect(model().attributeHasFieldErrorCode("trade", "account", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("trade", "type", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("trade", "buyQuantity", "NotNull"))
		;
	}
	
	
	@WithMockUser
	@Test
	void POST_tradeDelete_shouldSucceedWithRedirection() throws Exception {
		//ARRANGE:
		when(tradeService.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT+ASSERT:
		mockMvc.perform(get("/trade/delete/1")
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/trade/list"))
		;
		
		verify(tradeService).deleteById(1);
	}
	
	@WithMockUser
	@Test
	void GET_tradeDelete_IdDoesNotExist_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(tradeService.existsById(1)).thenReturn(Boolean.FALSE);
		
		//ACT+ASSERT:
		mockMvc.perform(get("/trade/delete/1")
					)
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("error"))
		.andExpect(model().attributeExists("errorMsg"))
		;
		
		//User must not be deleted
		verify(tradeService,never()).deleteById(1);
	}
	
}
