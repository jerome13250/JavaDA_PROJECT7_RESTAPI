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

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import com.nnk.springboot.testconfig.SpringWebIntegrationTestConfig;


/**
 * Integration test class for RatingController.
 * <p>This test uses a test database that is defined in <b>\src\test\resources\application.properties</b></p>
 * 
 * @author jerome
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(SpringWebIntegrationTestConfig.class)
class RatingControllerIT{

	Logger logger = LoggerFactory.getLogger(RatingControllerIT.class);
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext context;
	
	@Autowired
	private RatingService ratingService;
	
	Rating rating1;
	
	//When using @SpringBootTest annotation to test controllers with Spring Security, it's necessary to explicitly
	//configure the filter chain when setting up MockMvc:
	@BeforeEach
	public void setup() {
	     mockMvc = MockMvcBuilders
	    .webAppContextSetup(context)
	    .apply(springSecurity())
	    .build();
	     
	     rating1 = new Rating("A+", "A", "A++", 123);
	   
	}
	
	
	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void givenMockUser_shouldSucceedWith200() throws Exception {
		//ACT+ASSERT:
		mockMvc.perform(get("/rating/list"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("rating/list"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("listofRating"))
		;
	}
	
	@Transactional // rollback will be done automatically
	@WithMockUser //we have default values "user","password","USER_ROLE"
	@Test
	void POST_ratingValidate_shouldSucceedWithRedirection() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(post("/rating/validate")
				.param("moodysRating", "D-")
				.param("sandPRating", "F")
				.param("fitchRating", "D+")
				.param("orderNumber", "111")
				.with(csrf())
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/rating/list"))
		;
		
		List<Rating> listofRating = ratingService.findAll();
		assertEquals(1,listofRating.size()); //since we are in an empty test database 
		assertEquals("D-",listofRating.get(0).getMoodysRating());
		assertEquals("F",listofRating.get(0).getSandPRating());
		assertEquals("D+",listofRating.get(0).getFitchRating());
		assertEquals(111,listofRating.get(0).getOrderNumber());
	}
	
	@Transactional // rollback will be done automatically
	@WithMockUser
	@Test
	void POST_ratingUpdate_shouldSucceedWithRedirection() throws Exception {
		//insert a rating to update in the database:
		ratingService.save(rating1);
		//With a @GeneratedValue type id you can't know that value in advance (before actually writing it).
		//However once you persist your Bean, the id field will be populated in that bean instance and you can obtain
		//it without needing to do an extra query for it : 
		int id = rating1.getId();
		
		//ACT+ASSERT:
		mockMvc.perform(post("/rating/update/" + id)
				.param("moodysRating", "D-")
				.param("sandPRating", "F")
				.param("fitchRating", "D+")
				.param("orderNumber", "111")
				.with(csrf())
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/rating/list"))
		;
		
		Optional<Rating> resultRating = ratingService.findById(id);
		assertTrue(resultRating.isPresent());
		assertEquals("D-",resultRating.get().getMoodysRating());
		assertEquals("F",resultRating.get().getSandPRating());
		assertEquals("D+",resultRating.get().getFitchRating());
		assertEquals(111,resultRating.get().getOrderNumber());
	}

	@Transactional // rollback will be done automatically
	@WithMockUser
	@Test
	void GET_ratingDelete_IdDoesNotExist_shouldReturnErrorPage() throws Exception {
		//insert a rating to delete in the database:
		ratingService.save(rating1);
		//With a @GeneratedValue type id you can't know that value in advance (before actually writing it).
		//However once you persist your Bean, the id field will be populated in that bean instance and you can obtain
		//it without needing to do an extra query for it : 
		int id = rating1.getId();
		
		//ACT+ASSERT:
		mockMvc.perform(get("/rating/delete/" + id)
					)
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/rating/list"))
		;
		
		Optional<Rating> resultRating = ratingService.findById(id);
		assertFalse(resultRating.isPresent());
	}
	
}
