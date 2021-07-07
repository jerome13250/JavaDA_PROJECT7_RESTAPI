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

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;


/**
 * Integration test class for CurvePointController.
 * <p>This test uses a test database that is defined in <b>\src\test\resources\application.properties</b></p>
 * 
 * @author jerome
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
class CurveControllerTestIT{

	Logger logger = LoggerFactory.getLogger(CurveControllerTestIT.class);
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext context;
	
	@Autowired
	private CurvePointService curveService;
	
	CurvePoint curve1;
	
	//When using @SpringBootTest annotation to test controllers with Spring Security, it's necessary to explicitly
	//configure the filter chain when setting up MockMvc:
	@BeforeEach
	public void setup() {
	     mockMvc = MockMvcBuilders
	    .webAppContextSetup(context)
	    .apply(springSecurity())
	    .build();
	     
	     curve1 = new CurvePoint(100, 111.11d, 12.3d);
	   
	}
	
	
	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void givenMockUser_shouldSucceedWith200() throws Exception {
		//ACT+ASSERT:
		mockMvc.perform(get("/curvePoint/list"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("curvePoint/list"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("listofCurvePoint"))
		;
	}
	
	@Transactional // rollback will be done automatically
	@WithMockUser //we have default values "user","password","USER_ROLE"
	@Test
	void POST_curveValidate_shouldSucceedWithRedirection() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(post("/curvePoint/validate")
				.param("curveId", "127")
				.param("term", "123.4")
				.param("value", "567.8")
				.with(csrf())
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/curvePoint/list"))
		;
		
		List<CurvePoint> listofCurvePoint = curveService.findAll();
		assertEquals(1,listofCurvePoint.size()); //since we are in an empty test database 
		assertEquals(127,listofCurvePoint.get(0).getCurveId());
		assertEquals(123.4,listofCurvePoint.get(0).getTerm());
		assertEquals(567.8,listofCurvePoint.get(0).getValue());
	}
	
	@Transactional // rollback will be done automatically
	@WithMockUser
	@Test
	void POST_curveUpdate_shouldSucceedWithRedirection() throws Exception {
		//insert a curve to update in the database:
		curveService.save(curve1);
		//With a @GeneratedValue type id you can't know that value in advance (before actually writing it).
		//However once you persist your Bean, the id field will be populated in that bean instance and you can obtain
		//it without needing to do an extra query for it : 
		int id = curve1.getId();
		
		//ACT+ASSERT:
		mockMvc.perform(post("/curvePoint/update/"+id)
				.param("curveId", "127")
				.param("term", "123.4")
				.param("value", "567.8")
				.with(csrf())
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/curvePoint/list"))
		;
		
		Optional<CurvePoint> resultCurvePoint = curveService.findById(id);
		assertTrue(resultCurvePoint.isPresent());
		assertEquals(127,resultCurvePoint.get().getCurveId());
		assertEquals(123.4,resultCurvePoint.get().getTerm());
		assertEquals(567.8,resultCurvePoint.get().getValue());
	}

	@Transactional // rollback will be done automatically
	@WithMockUser
	@Test
	void GET_curveDelete_IdDoesNotExist_shouldReturnErrorPage() throws Exception {
		//insert a curve to delete in the database:
		curveService.save(curve1);
		//With a @GeneratedValue type id you can't know that value in advance (before actually writing it).
		//However once you persist your Bean, the id field will be populated in that bean instance and you can obtain
		//it without needing to do an extra query for it : 
		int id = curve1.getId();
		
		//ACT+ASSERT:
		mockMvc.perform(get("/curvePoint/delete/" + id)
					)
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/curvePoint/list"))
		;
		
		Optional<CurvePoint> resultCurvePoint = curveService.findById(id);
		assertFalse(resultCurvePoint.isPresent());
	}
	
}
