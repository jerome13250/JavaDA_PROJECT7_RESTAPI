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

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;

/**
 * Unit test class for RatingService
 * @author jerome
 *
 */
@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {
	
	@InjectMocks
	RatingServiceImpl ratingServiceImpl;
	
	@Mock
	RatingRepository ratingRepository;
	
	Rating rating1;
	Optional<Rating> optrating1;
	List<Rating> ListOfRating;
	
	@BeforeEach
	void initialize() {
		Rating rating1 = new Rating("Moodys Rating1", "Sand PRating1", "Fitch Rating1", 10);
		optrating1 = Optional.of(rating1);
		Rating rating2 = new Rating("Moodys Rating2", "Sand PRating2", "Fitch Rating2", 20);
		ListOfRating = new ArrayList<>();
		ListOfRating.add(rating1);
		ListOfRating.add(rating2);
	}
	
	@Test
	void test_findAll() {
		//ARRANGE:
		when(ratingRepository.findAll()).thenReturn(ListOfRating);
		
		//ACT:
		List<Rating> resultListOfRating = ratingServiceImpl.findAll();
		
		//ASSERT:
		assertEquals(ListOfRating, resultListOfRating);
	}
	
	@Test
	void test_save() {
		//ACT:
		ratingServiceImpl.save(rating1);
		
		//ASSERT:
		verify(ratingRepository).save(rating1);
	}
	
	@Test
	void test_findById() {
		//ARRANGE:
		when(ratingRepository.findById(1)).thenReturn(optrating1);
		
		//ACT:
		Optional<Rating> resultOptRating = ratingServiceImpl.findById(1);
		
		//ASSERT:
		assertEquals(optrating1, resultOptRating);
	}

	@Test
	void test_existsById() {
		//ARRANGE:
		when(ratingRepository.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT:
		Boolean result = ratingServiceImpl.existsById(1);
		
		//ASSERT:
		assertTrue(result);
	}
	
	@Test
	void test_deleteById() {
		//ACT:
		ratingServiceImpl.deleteById(1);
		
		//ASSERT:
		verify(ratingRepository).deleteById(1);
	}

}
