package com.nnk.springboot.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.TradeService;

@Service
public class TradeServiceImpl implements TradeService {

	@Autowired
	private TradeRepository tradeRepository;
	
	@Override
	public List<Trade> findAll() {
		return tradeRepository.findAll();
	}

	@Override
	public void save(Trade trade) {
		tradeRepository.save(trade);
	}

	@Override
	public Optional<Trade> findById(int id) {
		return tradeRepository.findById(id);
	}

	@Override
	public Boolean existsById(int id) {
		return tradeRepository.existsById(id);
	}

	@Override
	public void deleteById(int id) {
		tradeRepository.deleteById(id);
		
	}

}
