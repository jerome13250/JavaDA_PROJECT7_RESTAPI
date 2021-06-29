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

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
import com.nnk.springboot.testconfig.SpringWebTestConfig;

//@WebMvcTest tells Spring Boot to instantiate only the web layer and not the entire context
@WebMvcTest(controllers = CurveController.class) 
//Need to create a UserDetailsService in SpringSecurityWebTestConfig.class because @Service are not loaded by @WebMvcTest :
@Import(SpringWebTestConfig.class)

class CurveControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private CurvePointService curvePointService;
	
	CurvePoint curve1;
	List<CurvePoint> CurvePointList;
	
	@BeforeEach
	void initialize() {
		curve1 = new CurvePoint(100, 111.11d, 12.3d);
		curve1.setId(1);
		CurvePoint curve2 = new CurvePoint(200, 222.22d, 45.6d);
		curve2.setId(2);
		CurvePoint curve3 = new CurvePoint(300, 333.33d, 78.9d);
		curve3.setId(3);
		CurvePointList = new ArrayList<>();
		CurvePointList.add(curve1);
		CurvePointList.add(curve2);
		CurvePointList.add(curve3);		
		
	}
	
	@Test
	void givenNotLogged_shouldBeRedirectedToLogin() throws Exception {
		mockMvc.perform(get("/curvePoint/list"))
		.andExpect(status().isFound())
		;
	}

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void givenMockUser_shouldSucceedWith200() throws Exception {
		//ARRANGE:
		when(curvePointService.findAll()).thenReturn(CurvePointList);
		//ACT+ASSERT:
		mockMvc.perform(get("/curvePoint/list"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("curvePoint/list"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("listofCurvePoint"))
		;
	}

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void GET_addBidForm_shouldSucceedWith200() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(get("/curvePoint/add"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("curvePoint/add"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("curvePoint"))
		;
	}
	
	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void POST_curvePointValidate_shouldSucceedWithRedirection() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(post("/curvePoint/validate")
				.param("curveId", "999")
				.param("term", "123.4")
				.param("value", "567.8")
				.with(csrf())
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/curvePoint/list"))
		;
		
		ArgumentCaptor<CurvePoint> curvePointCaptor = ArgumentCaptor.forClass(CurvePoint.class);
		verify(curvePointService).save(curvePointCaptor.capture());
		CurvePoint savedCurvePoint = curvePointCaptor.getValue();
		assertEquals(999,savedCurvePoint.getCurveId());
		assertEquals(123.4,savedCurvePoint.getTerm());
		assertEquals(567.8,savedCurvePoint.getValue());
		
	}

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void POST_curvePointValidate_formValidationFailed_NoData() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(post("/curvePoint/validate")
				//.param("curveId", "999")
				//.param("term", "123.4")
				//.param("value", "567.8")
				.with(csrf())
				)
		.andExpect(status().isOk()) //return to validate page to display error
		.andExpect(view().name("/curvePoint/add"))
		.andExpect(model().size(1))
		.andExpect(model().attributeErrorCount("curvePoint", 3))
		.andExpect(model().attributeHasFieldErrorCode("curvePoint", "curveId", "NotNull"))
		.andExpect(model().attributeHasFieldErrorCode("curvePoint", "term", "NotNull"))
		.andExpect(model().attributeHasFieldErrorCode("curvePoint", "value", "NotNull"))
		;
		
		//CurvePoint must not be saved
		verify(curvePointService,never()).save(any(CurvePoint.class));
		
	}
	

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void GET_showUpdateForm_shouldSucceedWith200() throws Exception {
		//ARRANGE:
		when(curvePointService.findById(1)).thenReturn(Optional.of(curve1));
		
		//ACT+ASSERT:
		mockMvc.perform(get("/curvePoint/update/1"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("curvePoint/update"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("curvePoint"))
		;
	}
	
	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void GET_showUpdateForm_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(curvePointService.findById(1)).thenReturn(Optional.empty());
		
		//ACT+ASSERT:
		mockMvc.perform(get("/curvePoint/update/1"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("error"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("errorMsg"))
		;
	}
	
	@WithMockUser
	@Test
	void POST_curvePointUpdate_shouldSucceedWithRedirection() throws Exception {
		//ARRANGE:
		when(curvePointService.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT+ASSERT:
		mockMvc.perform(post("/curvePoint/update/1")
				.param("curveId", "999")
				.param("term", "123.4")
				.param("value", "567.8")
				.with(csrf())
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/curvePoint/list"))
		;
		
		ArgumentCaptor<CurvePoint> curvePointCaptor = ArgumentCaptor.forClass(CurvePoint.class);
		verify(curvePointService).save(curvePointCaptor.capture());
		CurvePoint savedCurvePoint = curvePointCaptor.getValue();
		assertEquals(1,savedCurvePoint.getId());
		assertEquals(999,savedCurvePoint.getCurveId());
		assertEquals(123.4,savedCurvePoint.getTerm());
		assertEquals(567.8,savedCurvePoint.getValue());
	}
	
	@WithMockUser
	@Test
	void POST_curvePointUpdate_IdDoesNotExist_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(curvePointService.existsById(1)).thenReturn(Boolean.FALSE);
		
		//ACT+ASSERT:
		mockMvc.perform(post("/curvePoint/update/1")
				.param("curveId", "999")
				.param("term", "123.4")
				.param("value", "567.8")
				.with(csrf())
				)
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("error"))
		.andExpect(model().attributeExists("errorMsg"))
		;
	}
	
	@WithMockUser
	@Test
	void POST_curvePointUpdate_FormValidationFail_NoDAta_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(curvePointService.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT+ASSERT:
		mockMvc.perform(post("/curvePoint/update/1")
				//.param("curveId", "999")
				//.param("term", "123.4")
				//.param("value", "567.8")
				.with(csrf())
				)
		.andExpect(status().isOk()) //return to validate page to display error
		.andExpect(view().name("curvePoint/update"))
		.andExpect(model().size(1))
		.andExpect(model().attributeErrorCount("curvePoint", 3))
		.andExpect(model().attributeHasFieldErrorCode("curvePoint", "curveId", "NotNull"))
		.andExpect(model().attributeHasFieldErrorCode("curvePoint", "term", "NotNull"))
		.andExpect(model().attributeHasFieldErrorCode("curvePoint", "value", "NotNull"))
		;
	}
	
	
	@WithMockUser
	@Test
	void POST_curvePointDelete_shouldSucceedWithRedirection() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(get("/curvePoint/delete/1")
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/curvePoint/list"))
		;
		
		verify(curvePointService).deleteById(1);
		
	}
	
}
