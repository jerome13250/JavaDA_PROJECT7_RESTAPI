package com.nnk.springboot.controllers.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
import com.nnk.springboot.testconfig.SpringWebIntegrationTestConfig;


/**
 * Integration test class for TradeController.
 * <p>This test uses a test database that is defined in <b>\src\test\resources\application.properties</b></p>
 * 
 * @author jerome
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(SpringWebIntegrationTestConfig.class)
class TradeControllerIT{

	Logger logger = LoggerFactory.getLogger(TradeControllerIT.class);
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext context;
	
	@Autowired
	private TradeService tradeService;
	
	Trade trade1;
	
	//When using @SpringBootTest annotation to test controllers with Spring Security, it's necessary to explicitly
	//configure the filter chain when setting up MockMvc:
	@BeforeEach
	public void setup() {
	     mockMvc = MockMvcBuilders
	    .webAppContextSetup(context)
	    .apply(springSecurity())
	    .build();
	     
	     trade1 = new Trade("account1", "type1", 111.11);
	   
	}
	
	
	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void givenMockUser_shouldSucceedWith200() throws Exception {
		//ACT+ASSERT:
		mockMvc.perform(get("/trade/list"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("trade/list"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("listofTrade"))
		;
	}
	
	@Transactional // rollback will be done automatically
	@WithMockUser //we have default values "user","password","USER_ROLE"
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
		
		List<Trade> listofTrade = tradeService.findAll();
		assertEquals(1,listofTrade.size()); //since we are in an empty test database 
		assertEquals("account_999",listofTrade.get(0).getAccount());
		assertEquals("type_999",listofTrade.get(0).getType());
		assertEquals(999.99,listofTrade.get(0).getBuyQuantity());
	}
	
	@Transactional // rollback will be done automatically
	@WithMockUser
	@Test
	void POST_tradeUpdate_shouldSucceedWithRedirection() throws Exception {
		//insert a trade to update in the database:
		tradeService.save(trade1);
		//With a @GeneratedValue type id you can't know that value in advance (before actually writing it).
		//However once you persist your Bean, the id field will be populated in that bean instance and you can obtain
		//it without needing to do an extra query for it : 
		int id = trade1.getTradeId();
		
		//ACT+ASSERT:
		mockMvc.perform(post("/trade/update/" + id)
				.param("account", "account_999")
				.param("type", "type_999")
				.param("buyQuantity", "999.99")
				.with(csrf())
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/trade/list"))
		;
		
		Optional<Trade> resultTrade = tradeService.findById(id);
		assertTrue(resultTrade.isPresent());
		assertEquals("account_999",resultTrade.get().getAccount());
		assertEquals("type_999",resultTrade.get().getType());
		assertEquals(999.99,resultTrade.get().getBuyQuantity());
	}

	@Transactional // rollback will be done automatically
	@WithMockUser
	@Test
	void GET_tradeDelete_IdDoesNotExist_shouldReturnErrorPage() throws Exception {
		//insert a trade to delete in the database:
		tradeService.save(trade1);
		//With a @GeneratedValue type id you can't know that value in advance (before actually writing it).
		//However once you persist your Bean, the id field will be populated in that bean instance and you can obtain
		//it without needing to do an extra query for it : 
		int id = trade1.getTradeId();
		
		//ACT+ASSERT:
		mockMvc.perform(get("/trade/delete/" + id)
					)
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/trade/list"))
		;
		
		Optional<Trade> resultTrade = tradeService.findById(id);
		assertFalse(resultTrade.isPresent());
	}
	
}
