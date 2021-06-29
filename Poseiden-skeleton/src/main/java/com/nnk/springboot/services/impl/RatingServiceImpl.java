package com.nnk.springboot.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.RatingService;

@Service
public class RatingServiceImpl implements RatingService {

	@Autowired
	private RatingRepository ratingRepository;
	
	@Override
	public List<Rating> findAll() {
		return ratingRepository.findAll();
	}

	@Override
	public void save(Rating rating) {
		ratingRepository.save(rating);
	}

	@Override
	public Optional<Rating> findById(int id) {
		return ratingRepository.findById(id);
	}

	@Override
	public Boolean existsById(int id) {
		return ratingRepository.existsById(id);
	}

	@Override
	public void deleteById(int id) {
		ratingRepository.deleteById(id);
		
	}

}
