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

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
import com.nnk.springboot.testconfig.SpringWebIntegrationTestConfig;


/**
 * Integration test class for RuleNameController.
 * <p>This test uses a test database that is defined in <b>\src\test\resources\application.properties</b></p>
 * 
 * @author jerome
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(SpringWebIntegrationTestConfig.class)
class RuleNameControllerIT{

	Logger logger = LoggerFactory.getLogger(RuleNameControllerIT.class);
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext context;
	
	@Autowired
	private RuleNameService ruleNameService;
	
	RuleName ruleName1;
	
	//When using @SpringBootTest annotation to test controllers with Spring Security, it's necessary to explicitly
	//configure the filter chain when setting up MockMvc:
	@BeforeEach
	public void setup() {
	     mockMvc = MockMvcBuilders
	    .webAppContextSetup(context)
	    .apply(springSecurity())
	    .build();
	     
	     ruleName1 = new RuleName("name1", "description1", "json1", "template1", "sqlStr1", "sqlPart1");
	   
	}
	
	
	@WithMockUser //annotation to test spring security with mock user : here we have default values "user","password","USER_ROLE"
	@Test
	void givenMockUser_shouldSucceedWith200() throws Exception {
		//ACT+ASSERT:
		mockMvc.perform(get("/ruleName/list"))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("ruleName/list"))
		.andExpect(model().size(1))
		.andExpect(model().attributeExists("listofRuleName"))
		;
	}
	
	@Transactional // rollback will be done automatically
	@WithMockUser //we have default values "user","password","USER_ROLE"
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
		
		List<RuleName> listofRuleName = ruleNameService.findAll();
		assertEquals(1,listofRuleName.size()); //since we are in an empty test database 
		assertEquals("name",listofRuleName.get(0).getName());
		assertEquals("description",listofRuleName.get(0).getDescription());
		assertEquals("json",listofRuleName.get(0).getJson());
		assertEquals("template",listofRuleName.get(0).getTemplate());
		assertEquals("sqlStr",listofRuleName.get(0).getSqlStr());
		assertEquals("sqlPart",listofRuleName.get(0).getSqlPart());
	}
	
	@Transactional // rollback will be done automatically
	@WithMockUser
	@Test
	void POST_ruleNameUpdate_shouldSucceedWithRedirection() throws Exception {
		//insert a ruleName to update in the database:
		ruleNameService.save(ruleName1);
		//With a @GeneratedValue type id you can't know that value in advance (before actually writing it).
		//However once you persist your Bean, the id field will be populated in that bean instance and you can obtain
		//it without needing to do an extra query for it : 
		int id = ruleName1.getId();
		
		//ACT+ASSERT:
		mockMvc.perform(post("/ruleName/update/" + id)
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
		
		Optional<RuleName> resultRuleName = ruleNameService.findById(id);
		assertTrue(resultRuleName.isPresent());
		assertEquals("name",resultRuleName.get().getName());
		assertEquals("description",resultRuleName.get().getDescription());
		assertEquals("json",resultRuleName.get().getJson());
		assertEquals("template",resultRuleName.get().getTemplate());
		assertEquals("sqlStr",resultRuleName.get().getSqlStr());
		assertEquals("sqlPart",resultRuleName.get().getSqlPart());
	}

	@Transactional // rollback will be done automatically
	@WithMockUser
	@Test
	void GET_ruleNameDelete_IdDoesNotExist_shouldReturnErrorPage() throws Exception {
		//insert a ruleName to delete in the database:
		ruleNameService.save(ruleName1);
		//With a @GeneratedValue type id you can't know that value in advance (before actually writing it).
		//However once you persist your Bean, the id field will be populated in that bean instance and you can obtain
		//it without needing to do an extra query for it : 
		int id = ruleName1.getId();
		
		//ACT+ASSERT:
		mockMvc.perform(get("/ruleName/delete/" + id)
					)
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/ruleName/list"))
		;
		
		Optional<RuleName> resultRuleName = ruleNameService.findById(id);
		assertFalse(resultRuleName.isPresent());
	}
	
}
