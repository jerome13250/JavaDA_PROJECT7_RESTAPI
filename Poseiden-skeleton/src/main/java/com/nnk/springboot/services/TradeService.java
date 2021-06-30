package com.nnk.springboot.services;

import java.util.List;
import java.util.Optional;

import com.nnk.springboot.domain.Trade;

/**
 * Service for the Trade object.
 * @author jerome
 *
 */
public interface TradeService {
	
	/**
	 * Find and return all Trade objects from our persistence layer.
	 * @return List of Trade
	 */
	public List<Trade> findAll();
	
	/**
	 * Save a Trade in our persistence layer.
	 * @param trade to save
	 */
	public void save(Trade trade);
	
	/**
	 * Find and return a Trade by id.
	 * @param id of the Trade
	 * @return the Trade object
	 */
	public Optional<Trade> findById(int id);
	
	/**
	 * Check if a Trade with specific id exist in our persistence layer.
	 * @param id of the Trade
	 * @return true if exists, false otherwise.
	 */
	public Boolean existsById(int id);
	
	/**
	 * Delete a Trade with specific id in our persistence layer.
	 * @param id of the Trade
	 */
	public void deleteById(int id);
	
}
