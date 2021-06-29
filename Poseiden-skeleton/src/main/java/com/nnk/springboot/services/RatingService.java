package com.nnk.springboot.services;

import java.util.List;
import java.util.Optional;

import com.nnk.springboot.domain.Rating;

/**
 * Service for the Rating object.
 * @author jerome
 *
 */
public interface RatingService {
	
	/**
	 * Find and return all Rating objects from our persistence layer.
	 * @return List of Rating
	 */
	public List<Rating> findAll();
	
	/**
	 * Save a Rating in our persistence layer.
	 * @param rating to save
	 */
	public void save(Rating rating);
	
	/**
	 * Find and return a Rating by id.
	 * @param id of the Rating
	 * @return the Rating object
	 */
	public Optional<Rating> findById(int id);
	
	/**
	 * Check if a Rating with specific id exist in our persistence layer.
	 * @param id of the Rating
	 * @return true if exists, false otherwise.
	 */
	public Boolean existsById(int id);
	
	/**
	 * Delete a Rating with specific id in our persistence layer.
	 * @param id of the Rating
	 */
	public void deleteById(int id);
	
}
