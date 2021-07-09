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

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import com.nnk.springboot.testconfig.SpringWebUnitTestConfig;

//@WebMvcTest tells Spring Boot to instantiate only the web layer and not the entire context
@WebMvcTest(controllers = RatingController.class) 
//Need to create a UserDetailsService in SpringSecurityWebTestConfig.class because @Service are not loaded by @WebMvcTest :
@Import(SpringWebUnitTestConfig.class)

class RatingControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RatingService ratingService;
	
	Rating rating1;
	List<Rating> RatingList;
	
	@BeforeEach
	void initialize() {
		rating1 = new Rating("A+", "A", "A++", 123);
		rating1.setId(1);
		Rating rating2 = new Rating("B+", "A-", "B", 456);
		rating2.setId(2);
		Rating rating3 = new Rating("C", "C-", "D", 789);
		rating3.setId(3);
		RatingList = new ArrayList<>();
		RatingList.add(rating1);
		RatingList.add(rating2);
		RatingList.add(rating3);		
		
	}
	
	@Test
	void givenNotLogged_shouldBeRedirectedToLogin() throws Exception {
		mockMvc.perform(get("/rating/list"))
		.andExpect(status().isFound())
		;
	}

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void givenMockUser_shouldSucceedWith200() throws Exception {
		//ARRANGE:
		when(ratingService.findAll()).thenReturn(RatingList);
		//ACT+ASSERT:
		mockMvc.perform(get("/rating/list"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("rating/list"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("listofRating"))
		;
	}

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void GET_addBidForm_shouldSucceedWith200() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(get("/rating/add"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("rating/add"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("rating"))
		;
	}
	
	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
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
		
		ArgumentCaptor<Rating> ratingCaptor = ArgumentCaptor.forClass(Rating.class);
		verify(ratingService).save(ratingCaptor.capture());
		Rating savedRating = ratingCaptor.getValue();
		assertEquals("D-",savedRating.getMoodysRating());
		assertEquals("F",savedRating.getSandPRating());
		assertEquals("D+",savedRating.getFitchRating());
		assertEquals(111,savedRating.getOrderNumber());
		
	}

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void POST_ratingValidate_formValidationFailed_NoData() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(post("/rating/validate")
				//.param("moodysRating", "D-")
				//.param("sandPRating", "F")
				//.param("fitchRating", "D+")
				//.param("orderNumber", "111")
				.with(csrf())
				)
		.andExpect(status().isOk()) //return to validate page to display error
		.andExpect(view().name("/rating/add"))
		.andExpect(model().size(1))
		.andExpect(model().attributeErrorCount("rating", 4))
		.andExpect(model().attributeHasFieldErrorCode("rating", "moodysRating", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("rating", "sandPRating", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("rating", "fitchRating", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("rating", "orderNumber", "NotNull"))
		;
		
		//Rating must not be saved
		verify(ratingService,never()).save(any(Rating.class));
		
	}
	

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void GET_showUpdateForm_shouldSucceedWith200() throws Exception {
		//ARRANGE:
		when(ratingService.findById(1)).thenReturn(Optional.of(rating1));
		
		//ACT+ASSERT:
		mockMvc.perform(get("/rating/update/1"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("rating/update"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("rating"))
		;
	}
	
	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void GET_showUpdateForm_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(ratingService.findById(1)).thenReturn(Optional.empty());
		
		//ACT+ASSERT:
		mockMvc.perform(get("/rating/update/1"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("error"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("errorMsg"))
		;
	}
	
	@WithMockUser
	@Test
	void POST_ratingUpdate_shouldSucceedWithRedirection() throws Exception {
		//ARRANGE:
		when(ratingService.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT+ASSERT:
		mockMvc.perform(post("/rating/update/1")
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
		
		ArgumentCaptor<Rating> ratingCaptor = ArgumentCaptor.forClass(Rating.class);
		verify(ratingService).save(ratingCaptor.capture());
		Rating savedRating = ratingCaptor.getValue();
		assertEquals(1,savedRating.getId());
		assertEquals("D-",savedRating.getMoodysRating());
		assertEquals("F",savedRating.getSandPRating());
		assertEquals("D+",savedRating.getFitchRating());
		assertEquals(111,savedRating.getOrderNumber());
	}
	
	@WithMockUser
	@Test
	void POST_ratingUpdate_IdDoesNotExist_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(ratingService.existsById(1)).thenReturn(Boolean.FALSE);
		
		//ACT+ASSERT:
		mockMvc.perform(post("/rating/update/1")
				.param("moodysRating", "D-")
				.param("sandPRating", "F")
				.param("fitchRating", "D+")
				.param("orderNumber", "111")
				.with(csrf())
				)
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("error"))
		.andExpect(model().attributeExists("errorMsg"))
		;
	}
	
	@WithMockUser
	@Test
	void POST_ratingUpdate_FormValidationFail_NoDAta_shouldReturnToUpdatePage() throws Exception {
		//ARRANGE:
		when(ratingService.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT+ASSERT:
		mockMvc.perform(post("/rating/update/1")
				//.param("moodysRating", "D-")
				//.param("sandPRating", "F")
				//.param("fitchRating", "D+")
				//.param("orderNumber", "111")
				.with(csrf())
				)
		.andExpect(status().isOk()) //return to validate page to display error
		.andExpect(view().name("rating/update"))
		.andExpect(model().size(1))
		.andExpect(model().attributeErrorCount("rating", 4))
		.andExpect(model().attributeHasFieldErrorCode("rating", "moodysRating", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("rating", "sandPRating", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("rating", "fitchRating", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("rating", "orderNumber", "NotNull"))
		;
	}
	
	
	@WithMockUser
	@Test
	void POST_ratingDelete_shouldSucceedWithRedirection() throws Exception {
		//ARRANGE:
		when(ratingService.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT+ASSERT:
		mockMvc.perform(get("/rating/delete/1")
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/rating/list"))
		;
		
		verify(ratingService).deleteById(1);
		
	}
	
	@WithMockUser
	@Test
	void GET_ratingDelete_IdDoesNotExist_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(ratingService.existsById(1)).thenReturn(Boolean.FALSE);
		
		//ACT+ASSERT:
		mockMvc.perform(get("/rating/delete/1")
					)
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("error"))
		.andExpect(model().attributeExists("errorMsg"))
		;
		
		//User must not be deleted
		verify(ratingService,never()).deleteById(1);
	}
	
}
