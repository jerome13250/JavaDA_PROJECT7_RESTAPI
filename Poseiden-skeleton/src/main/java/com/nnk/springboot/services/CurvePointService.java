package com.nnk.springboot.services;

import java.util.List;
import java.util.Optional;

import com.nnk.springboot.domain.CurvePoint;

/**
 * Service for the CurvePoint object.
 * @author jerome
 *
 */
public interface CurvePointService {
	
	/**
	 * Find and return all CurvePoint objects from our persistence layer.
	 * @return List of CurvePoint
	 */
	public List<CurvePoint> findAll();
	
	/**
	 * Save a CurvePoint in our persistence layer.
	 * @param curvePoint to save
	 */
	public void save(CurvePoint curvePoint);
	
	/**
	 * Find and return a CurvePoint by id.
	 * @param id of the CurvePoint
	 * @return the CurvePoint object
	 */
	public Optional<CurvePoint> findById(int id);
	
	/**
	 * Check if a CurvePoint with specific id exist in our persistence layer.
	 * @param id of the CurvePoint
	 * @return true if exists, false otherwise.
	 */
	public Boolean existsById(int id);
	
	/**
	 * Delete a CurvePoint with specific id in our persistence layer.
	 * @param id of the CurvePoint
	 */
	public void deleteById(int id);
	
}
