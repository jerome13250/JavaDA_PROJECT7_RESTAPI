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

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
import com.nnk.springboot.testconfig.SpringWebTestConfig;

//@WebMvcTest tells Spring Boot to instantiate only the web layer and not the entire context
@WebMvcTest(controllers = RuleNameController.class) 
//Need to create a UserDetailsService in SpringSecurityWebTestConfig.class because @Service are not loaded by @WebMvcTest :
@Import(SpringWebTestConfig.class)

class RuleNameControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RuleNameService ruleNameService;
	
	RuleName ruleName1;
	List<RuleName> RuleNameList;
	
	@BeforeEach
	void initialize() {
		ruleName1 = new RuleName("name1", "description1", "json1", "template1", "sqlStr1", "sqlPart1");
		ruleName1.setId(1);
		RuleName ruleName2 = new RuleName("name2", "description2", "json2", "template2", "sqlStr2", "sqlPart2");
		ruleName2.setId(2);
		RuleName ruleName3 = new RuleName("name3", "description3", "json3", "template3", "sqlStr3", "sqlPart3");
		ruleName3.setId(3);
		RuleNameList = new ArrayList<>();
		RuleNameList.add(ruleName1);
		RuleNameList.add(ruleName2);
		RuleNameList.add(ruleName3);		
		
	}
	
	@Test
	void givenNotLogged_shouldBeRedirectedToLogin() throws Exception {
		mockMvc.perform(get("/ruleName/list"))
		.andExpect(status().isFound())
		;
	}

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void givenMockUser_shouldSucceedWith200() throws Exception {
		//ARRANGE:
		when(ruleNameService.findAll()).thenReturn(RuleNameList);
		//ACT+ASSERT:
		mockMvc.perform(get("/ruleName/list"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("ruleName/list"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("listofRuleName"))
		;
	}

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void GET_addBidForm_shouldSucceedWith200() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(get("/ruleName/add"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("ruleName/add"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("ruleName"))
		;
	}
	
	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void POST_ruleNameValidate_shouldSucceedWithRedirection() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(post("/ruleName/validate")
				.param("name", "name")
				.param("description", "description")
				.param("json", "json")
				.param("template", "template")
				.param("sqlStr", "sqlStr")
				.param("sqlPart", "sqlPart")
				.with(csrf())
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/ruleName/list"))
		;
		
		ArgumentCaptor<RuleName> ruleNameCaptor = ArgumentCaptor.forClass(RuleName.class);
		verify(ruleNameService).save(ruleNameCaptor.capture());
		RuleName savedRuleName = ruleNameCaptor.getValue();
		assertEquals("name",savedRuleName.getName());
		assertEquals("description",savedRuleName.getDescription());
		assertEquals("json",savedRuleName.getJson());
		assertEquals("template",savedRuleName.getTemplate());
		assertEquals("sqlStr",savedRuleName.getSqlStr());
		assertEquals("sqlPart",savedRuleName.getSqlPart());
		
	}

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void POST_ruleNameValidate_formValidationFailed_NoData() throws Exception {
		
		//ACT+ASSERT:
		mockMvc.perform(post("/ruleName/validate")
				//.param("name", "name")
				//.param("description", "description")
				//.param("json", "json")
				//.param("template", "template")
				//.param("sqlStr1", "sqlStr1")
				//.param("sqlPart", "sqlPart")
				.with(csrf())
				)
		.andExpect(status().isOk()) //return to validate page to display error
		.andExpect(view().name("/ruleName/add"))
		.andExpect(model().size(1))
		.andExpect(model().attributeErrorCount("ruleName", 6))
		.andExpect(model().attributeHasFieldErrorCode("ruleName", "name", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("ruleName", "description", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("ruleName", "json", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("ruleName", "template", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("ruleName", "sqlStr", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("ruleName", "sqlPart", "NotBlank"))
		;
		
		//RuleName must not be saved
		verify(ruleNameService,never()).save(any(RuleName.class));
		
	}
	

	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void GET_showUpdateForm_shouldSucceedWith200() throws Exception {
		//ARRANGE:
		when(ruleNameService.findById(1)).thenReturn(Optional.of(ruleName1));
		
		//ACT+ASSERT:
		mockMvc.perform(get("/ruleName/update/1"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("ruleName/update"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("ruleName"))
		;
	}
	
	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void GET_showUpdateForm_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(ruleNameService.findById(1)).thenReturn(Optional.empty());
		
		//ACT+ASSERT:
		mockMvc.perform(get("/ruleName/update/1"))
		.andDo(print())
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("error"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("errorMsg"))
		;
	}
	
	@WithMockUser
	@Test
	void POST_ruleNameUpdate_shouldSucceedWithRedirection() throws Exception {
		//ARRANGE:
		when(ruleNameService.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT+ASSERT:
		mockMvc.perform(post("/ruleName/update/1")
				.param("name", "name")
				.param("description", "description")
				.param("json", "json")
				.param("template", "template")
				.param("sqlStr", "sqlStr")
				.param("sqlPart", "sqlPart")
				.with(csrf())
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/ruleName/list"))
		;
		
		ArgumentCaptor<RuleName> ruleNameCaptor = ArgumentCaptor.forClass(RuleName.class);
		verify(ruleNameService).save(ruleNameCaptor.capture());
		RuleName savedRuleName = ruleNameCaptor.getValue();
		assertEquals(1,savedRuleName.getId());
		assertEquals("name",savedRuleName.getName());
		assertEquals("description",savedRuleName.getDescription());
		assertEquals("json",savedRuleName.getJson());
		assertEquals("template",savedRuleName.getTemplate());
		assertEquals("sqlStr",savedRuleName.getSqlStr());
		assertEquals("sqlPart",savedRuleName.getSqlPart());
	}
	
	@WithMockUser
	@Test
	void POST_ruleNameUpdate_IdDoesNotExist_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(ruleNameService.existsById(1)).thenReturn(Boolean.FALSE);
		
		//ACT+ASSERT:
		mockMvc.perform(post("/ruleName/update/1")
				.param("name", "name")
				.param("description", "description")
				.param("json", "json")
				.param("template", "template")
				.param("sqlStr1", "sqlStr1")
				.param("sqlPart", "sqlPart")
				.with(csrf())
				)
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("error"))
		.andExpect(model().attributeExists("errorMsg"))
		;
	}
	
	@WithMockUser
	@Test
	void POST_ruleNameUpdate_FormValidationFail_NoDAta_shouldReturnToUpdatePage() throws Exception {
		//ARRANGE:
		when(ruleNameService.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT+ASSERT:
		mockMvc.perform(post("/ruleName/update/1")
				//.param("name", "name")
				//.param("description", "description")
				//.param("json", "json")
				//.param("template", "template")
				//.param("sqlStr1", "sqlStr1")
				//.param("sqlPart", "sqlPart")
				.with(csrf())
				)
		.andExpect(status().isOk()) //return to validate page to display error
		.andExpect(view().name("ruleName/update"))
		.andExpect(model().size(1))
		.andExpect(model().attributeErrorCount("ruleName", 6))
		.andExpect(model().attributeHasFieldErrorCode("ruleName", "name", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("ruleName", "description", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("ruleName", "json", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("ruleName", "template", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("ruleName", "sqlStr", "NotBlank"))
		.andExpect(model().attributeHasFieldErrorCode("ruleName", "sqlPart", "NotBlank"))
		;
	}
	
	
	@WithMockUser
	@Test
	void POST_ruleNameDelete_shouldSucceedWithRedirection() throws Exception {
		//ARRANGE:
		when(ruleNameService.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT+ASSERT:
		mockMvc.perform(get("/ruleName/delete/1")
				)
		.andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/ruleName/list"))
		;
		
		verify(ruleNameService).deleteById(1);
		
	}
	
	@WithMockUser
	@Test
	void GET_ruleNameDelete_IdDoesNotExist_shouldReturnErrorPage() throws Exception {
		//ARRANGE:
		when(ruleNameService.existsById(1)).thenReturn(Boolean.FALSE);
		
		//ACT+ASSERT:
		mockMvc.perform(get("/ruleName/delete/1")
					)
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("error"))
		.andExpect(model().attributeExists("errorMsg"))
		;
		
		//User must not be deleted
		verify(ruleNameService,never()).deleteById(1);
	}
	
}
