package com.nnk.springboot.services;

import java.util.List;
import java.util.Optional;

import com.nnk.springboot.domain.CurvePoint;

public interface CurvePointService {

	public List<CurvePoint> findAll();
	public void save(CurvePoint curvePoint);
	public Optional<CurvePoint> findById(int id);
	public Boolean existsById(int id);
	public void deleteById(int id);
	
}
