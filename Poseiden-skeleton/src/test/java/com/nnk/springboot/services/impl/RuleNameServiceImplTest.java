package com.nnk.springboot.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;

/**
 * Unit test class for RuleNameService
 * @author jerome
 *
 */
@ExtendWith(MockitoExtension.class)
class RuleNameServiceImplTest {
	
	@InjectMocks
	RuleNameServiceImpl ruleNameServiceImpl;
	
	@Mock
	RuleNameRepository ruleNameRepository;
	
	RuleName ruleName1;
	Optional<RuleName> optruleName1;
	List<RuleName> ListOfRuleName;
	
	@BeforeEach
	void initialize() {
		RuleName ruleName1 = new RuleName("Rule Name1", "Description1", "Json1", "Template1", "SQL1", "SQL Part1");
		optruleName1 = Optional.of(ruleName1);
		RuleName ruleName2 = new RuleName("Rule Name2", "Description2", "Json2", "Template2", "SQL2", "SQL Part2");
		ListOfRuleName = new ArrayList<>();
		ListOfRuleName.add(ruleName1);
		ListOfRuleName.add(ruleName2);
	}
	
	@Test
	void test_findAll() {
		//ARRANGE:
		when(ruleNameRepository.findAll()).thenReturn(ListOfRuleName);
		
		//ACT:
		List<RuleName> resultListOfRuleName = ruleNameServiceImpl.findAll();
		
		//ASSERT:
		assertEquals(ListOfRuleName, resultListOfRuleName);
	}
	
	@Test
	void test_save() {
		//ACT:
		ruleNameServiceImpl.save(ruleName1);
		
		//ASSERT:
		verify(ruleNameRepository).save(ruleName1);
	}
	
	@Test
	void test_findById() {
		//ARRANGE:
		when(ruleNameRepository.findById(1)).thenReturn(optruleName1);
		
		//ACT:
		Optional<RuleName> resultOptRuleName = ruleNameServiceImpl.findById(1);
		
		//ASSERT:
		assertEquals(optruleName1, resultOptRuleName);
	}

	@Test
	void test_existsById() {
		//ARRANGE:
		when(ruleNameRepository.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT:
		Boolean result = ruleNameServiceImpl.existsById(1);
		
		//ASSERT:
		assertTrue(result);
	}
	
	@Test
	void test_deleteById() {
		//ACT:
		ruleNameServiceImpl.deleteById(1);
		
		//ASSERT:
		verify(ruleNameRepository).deleteById(1);
	}

}
