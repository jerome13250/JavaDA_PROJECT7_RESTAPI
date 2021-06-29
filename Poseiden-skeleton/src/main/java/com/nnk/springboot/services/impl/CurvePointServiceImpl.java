package com.nnk.springboot.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.CurvePointService;

@Service
public class CurvePointServiceImpl implements CurvePointService {

	@Autowired
	private CurvePointRepository curvePointRepository;
	
	@Override
	public List<CurvePoint> findAll() {
		return curvePointRepository.findAll();
	}

	@Override
	public void save(CurvePoint curvePoint) {
		curvePointRepository.save(curvePoint);
	}

	@Override
	public Optional<CurvePoint> findById(int id) {
		return curvePointRepository.findById(id);
	}

	@Override
	public Boolean existsById(int id) {
		return curvePointRepository.existsById(id);
	}

	@Override
	public void deleteById(int id) {
		curvePointRepository.deleteById(id);
		
	}

}
