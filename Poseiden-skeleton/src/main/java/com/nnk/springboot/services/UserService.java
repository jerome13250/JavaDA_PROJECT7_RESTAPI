package com.nnk.springboot.services;

import java.util.List;
import java.util.Optional;

import com.nnk.springboot.domain.User;

/**
 * Service for the User object.
 * @author jerome
 *
 */
public interface UserService {

	/**
	 * Find and return a User by user name. Required by Spring Security.
	 * @param username is the required user name.
	 * @return User with required user name.
	 */
	User findByUsername(String username);
	
	/**
	 * Check if a User exists in persistence layer with the required user name.
	 * @param username, the user name
	 * @return Boolean true if user name exists, false otherwise.
	 */
	Boolean existsByUsername(String username);
	
	/**
	 * Find and return all User objects from our persistence layer.
	 * @return List of User
	 */
	public List<User> findAll();
	
	/**
	 * Save a User in our persistence layer.
	 * @param user to save
	 */
	public void save(User user);
	
	/**
	 * Find and return a User by id.
	 * @param id of the User
	 * @return the User object
	 */
	public Optional<User> findById(int id);
	
	/**
	 * Check if a User with specific id exist in our persistence layer.
	 * @param id of the User
	 * @return true if exists, false otherwise.
	 */
	public Boolean existsById(int id);
	
	/**
	 * Delete a User with specific id in our persistence layer.
	 * @param id of the User
	 */
	public void deleteById(int id);
	
}