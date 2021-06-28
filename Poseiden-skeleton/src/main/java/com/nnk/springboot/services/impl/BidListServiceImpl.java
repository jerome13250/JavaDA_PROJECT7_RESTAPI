package com.nnk.springboot.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exception.BusinessResourceException;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.repositories.BidListRepository;

@Service
public class BidListServiceImpl implements BidListService {

	@Autowired
	private BidListRepository bidListRepository;
	
	@Override
	public List<BidList> findAll() {
		return bidListRepository.findAll();
	}

	@Override
	public void save(BidList bidList) {
		bidListRepository.save(bidList);
	}

	@Override
	public Optional<BidList> findById(Integer id) {
		return bidListRepository.findById(id);	
	}

	@Override
	public Boolean existsById(Integer id) {
		return bidListRepository.existsById(id);
	}

	@Override
	public void deleteById(Integer id) {
		bidListRepository.deleteById(id);
	}

}
