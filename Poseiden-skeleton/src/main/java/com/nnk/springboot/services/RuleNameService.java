package com.nnk.springboot.services;

import java.util.List;
import java.util.Optional;

import com.nnk.springboot.domain.RuleName;

/**
 * Service for the RuleName object.
 * @author jerome
 *
 */
public interface RuleNameService {
	
	/**
	 * Find and return all RuleName objects from our persistence layer.
	 * @return List of RuleName
	 */
	public List<RuleName> findAll();
	
	/**
	 * Save a RuleName in our persistence layer.
	 * @param ruleName to save
	 */
	public void save(RuleName ruleName);
	
	/**
	 * Find and return a RuleName by id.
	 * @param id of the RuleName
	 * @return the RuleName object
	 */
	public Optional<RuleName> findById(int id);
	
	/**
	 * Check if a RuleName with specific id exist in our persistence layer.
	 * @param id of the RuleName
	 * @return true if exists, false otherwise.
	 */
	public Boolean existsById(int id);
	
	/**
	 * Delete a RuleName with specific id in our persistence layer.
	 * @param id of the RuleName
	 */
	public void deleteById(int id);
	
}
