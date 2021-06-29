package com.nnk.springboot.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.RuleNameService;

@Service
public class RuleNameServiceImpl implements RuleNameService {

	@Autowired
	private RuleNameRepository ruleNameRepository;
	
	@Override
	public List<RuleName> findAll() {
		return ruleNameRepository.findAll();
	}

	@Override
	public void save(RuleName ruleName) {
		ruleNameRepository.save(ruleName);
	}

	@Override
	public Optional<RuleName> findById(int id) {
		return ruleNameRepository.findById(id);
	}

	@Override
	public Boolean existsById(int id) {
		return ruleNameRepository.existsById(id);
	}

	@Override
	public void deleteById(int id) {
		ruleNameRepository.deleteById(id);
		
	}

}
