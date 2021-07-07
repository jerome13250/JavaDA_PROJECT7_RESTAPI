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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;


/**
 * Integration test class for BidListController.
 * <p>This test uses a test database that is defined in <b>\src\test\resources\application.properties</b></p>
 * 
 * @author jerome
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
class BidListControllerTestIT{

	Logger logger = LoggerFactory.getLogger(BidListControllerTestIT.class);
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext context;
	
	@Autowired
	private BidListService bidListService;
	
	BidList bid1;
	
	//When using @SpringBootTest annotation to test controllers with Spring Security, it's necessary to explicitly
	//configure the filter chain when setting up MockMvc:
	@BeforeEach
	public void setup() {
	     mockMvc = MockMvcBuilders
	    .webAppContextSetup(context)
	    .apply(springSecurity())
	    .build();
	     
	     bid1 = new BidList("Account1", "Type1", 1d);
	   
	}
	
	
	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void givenMockUser_shouldSucceedWith200() throws Exception {
		//ACT+ASSERT:
		mockMvc.perform(get("/bidList/list"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("bidList/list"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("listofbidlist"))
		;
	}
	
	@Transactional // rollback will be done automatically
	@WithMockUser //we have default values "user","password","USER_ROLE"
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
		
		List<BidList> listofBidList = bidListService.findAll();
		assertEquals(1,listofBidList.size()); //since we are in an empty test database 
		assertEquals("accountOfBid",listofBidList.get(0).getAccount());
		assertEquals("typeOfBid",listofBidList.get(0).getType());
		assertEquals(1000d,listofBidList.get(0).getBidQuantity());
	}
	
	@Transactional // rollback will be done automatically
	@WithMockUser
	@Test
	void POST_bidListUpdate_shouldSucceedWithRedirection() throws Exception {
		//insert a bidList to update in the database:
		bidListService.save(bid1);
		//With a @GeneratedValue type id you can't know that value in advance (before actually writing it).
		//However once you persist your Bean, the id field will be populated in that bean instance and you can obtain
		//it without needing to do an extra query for it : 
		int id = bid1.getBidListId();
		
		//ACT+ASSERT:
		mockMvc.perform(post("/bidList/update/" + id)
				.param("account", "accountOfBid")
				.param("type", "typeOfBid")
				.param("bidQuantity", "1000")
				.with(csrf())
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/bidList/list"))
		;
		
		Optional<BidList> resultBidList = bidListService.findById(id);
		assertTrue(resultBidList.isPresent());
		assertEquals("accountOfBid",resultBidList.get().getAccount());
		assertEquals("typeOfBid",resultBidList.get().getType());
		assertEquals(1000d,resultBidList.get().getBidQuantity());
	}

	@Transactional // rollback will be done automatically
	@WithMockUser
	@Test
	void GET_bidListDelete_IdDoesNotExist_shouldReturnErrorPage() throws Exception {
		//insert a bidList to delete in the database:
		bidListService.save(bid1);
		//With a @GeneratedValue type id you can't know that value in advance (before actually writing it).
		//However once you persist your Bean, the id field will be populated in that bean instance and you can obtain
		//it without needing to do an extra query for it : 
		int id = bid1.getBidListId();
		
		//ACT+ASSERT:
		mockMvc.perform(get("/bidList/delete/" + id)
					)
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/bidList/list"))
		;
		
		Optional<BidList> resultBidList = bidListService.findById(id);
		assertFalse(resultBidList.isPresent());
	}
	
}
