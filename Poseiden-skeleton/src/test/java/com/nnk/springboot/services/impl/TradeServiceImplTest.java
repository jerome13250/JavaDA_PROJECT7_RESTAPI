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

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;

/**
 * Unit test class for TradeService
 * @author jerome
 *
 */
@ExtendWith(MockitoExtension.class)
class TradeServiceImplTest {
	
	@InjectMocks
	TradeServiceImpl tradeServiceImpl;
	
	@Mock
	TradeRepository tradeRepository;
	
	Trade trade1;
	Optional<Trade> opttrade1;
	List<Trade> ListOfTrade;
	
	@BeforeEach
	void initialize() {
		Trade trade1 = new Trade("Trade Account1", "Type1", 1d);
		opttrade1 = Optional.of(trade1);
		Trade trade2 = new Trade("Trade Account2", "Type2", 2d);
		ListOfTrade = new ArrayList<>();
		ListOfTrade.add(trade1);
		ListOfTrade.add(trade2);
	}
	
	@Test
	void test_findAll() {
		//ARRANGE:
		when(tradeRepository.findAll()).thenReturn(ListOfTrade);
		
		//ACT:
		List<Trade> resultListOfTrade = tradeServiceImpl.findAll();
		
		//ASSERT:
		assertEquals(ListOfTrade, resultListOfTrade);
	}
	
	@Test
	void test_save() {
		//ACT:
		tradeServiceImpl.save(trade1);
		
		//ASSERT:
		verify(tradeRepository).save(trade1);
	}
	
	@Test
	void test_findById() {
		//ARRANGE:
		when(tradeRepository.findById(1)).thenReturn(opttrade1);
		
		//ACT:
		Optional<Trade> resultOptTrade = tradeServiceImpl.findById(1);
		
		//ASSERT:
		assertEquals(opttrade1, resultOptTrade);
	}

	@Test
	void test_existsById() {
		//ARRANGE:
		when(tradeRepository.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT:
		Boolean result = tradeServiceImpl.existsById(1);
		
		//ASSERT:
		assertTrue(result);
	}
	
	@Test
	void test_deleteById() {
		//ACT:
		tradeServiceImpl.deleteById(1);
		
		//ASSERT:
		verify(tradeRepository).deleteById(1);
	}

}
