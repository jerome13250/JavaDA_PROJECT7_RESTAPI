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

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;

/**
 * Unit test class for BidListService
 * @author jerome
 *
 */
@ExtendWith(MockitoExtension.class)
class BidListServiceImplTest {
	
	@InjectMocks
	BidListServiceImpl bidListServiceImpl;
	
	@Mock
	BidListRepository bidListRepository;
	
	BidList bid1;
	Optional<BidList> optbid1;
	List<BidList> ListOfBidList;
	
	@BeforeEach
	void initialize() {
		BidList bid1 = new BidList("Account Test1", "Type Test1", 10d);
		optbid1 = Optional.of(bid1);
		BidList bid2 = new BidList("Account Test1", "Type Test1", 20d);
		ListOfBidList = new ArrayList<>();
		ListOfBidList.add(bid1);
		ListOfBidList.add(bid2);
	}
	
	@Test
	void test_findAll() {
		//ARRANGE:
		when(bidListRepository.findAll()).thenReturn(ListOfBidList);
		
		//ACT:
		List<BidList> resultListOfBidList = bidListServiceImpl.findAll();
		
		//ASSERT:
		assertEquals(ListOfBidList, resultListOfBidList);
	}
	
	@Test
	void test_save() {
		//ACT:
		bidListServiceImpl.save(bid1);
		
		//ASSERT:
		verify(bidListRepository).save(bid1);
	}
	
	@Test
	void test_findById() {
		//ARRANGE:
		when(bidListRepository.findById(1)).thenReturn(optbid1);
		
		//ACT:
		Optional<BidList> resultOptBidList = bidListServiceImpl.findById(1);
		
		//ASSERT:
		assertEquals(optbid1, resultOptBidList);
	}

	@Test
	void test_existsById() {
		//ARRANGE:
		when(bidListRepository.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT:
		Boolean result = bidListServiceImpl.existsById(1);
		
		//ASSERT:
		assertTrue(result);
	}
	
	@Test
	void test_deleteById() {
		//ACT:
		bidListServiceImpl.deleteById(1);
		
		//ASSERT:
		verify(bidListRepository).deleteById(1);
	}

}
