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

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;

/**
 * Unit test class for CurvePointService
 * @author jerome
 *
 */
@ExtendWith(MockitoExtension.class)
class CurvePointServiceImplTest {
	
	@InjectMocks
	CurvePointServiceImpl curvePointServiceImpl;
	
	@Mock
	CurvePointRepository curvePointRepository;
	
	CurvePoint curvePoint1;
	Optional<CurvePoint> optcurvePoint1;
	List<CurvePoint> ListOfCurvePoint;
	
	@BeforeEach
	void initialize() {
		CurvePoint curvePoint1 = new CurvePoint(10, 10d, 30d);
		optcurvePoint1 = Optional.of(curvePoint1);
		CurvePoint curvePoint2 = new CurvePoint(20, 20d, 60d);
		ListOfCurvePoint = new ArrayList<>();
		ListOfCurvePoint.add(curvePoint1);
		ListOfCurvePoint.add(curvePoint2);
	}
	
	@Test
	void test_findAll() {
		//ARRANGE:
		when(curvePointRepository.findAll()).thenReturn(ListOfCurvePoint);
		
		//ACT:
		List<CurvePoint> resultListOfCurvePoint = curvePointServiceImpl.findAll();
		
		//ASSERT:
		assertEquals(ListOfCurvePoint, resultListOfCurvePoint);
	}
	
	@Test
	void test_save() {
		//ACT:
		curvePointServiceImpl.save(curvePoint1);
		
		//ASSERT:
		verify(curvePointRepository).save(curvePoint1);
	}
	
	@Test
	void test_findById() {
		//ARRANGE:
		when(curvePointRepository.findById(1)).thenReturn(optcurvePoint1);
		
		//ACT:
		Optional<CurvePoint> resultOptCurvePoint = curvePointServiceImpl.findById(1);
		
		//ASSERT:
		assertEquals(optcurvePoint1, resultOptCurvePoint);
	}

	@Test
	void test_existsById() {
		//ARRANGE:
		when(curvePointRepository.existsById(1)).thenReturn(Boolean.TRUE);
		
		//ACT:
		Boolean result = curvePointServiceImpl.existsById(1);
		
		//ASSERT:
		assertTrue(result);
	}
	
	@Test
	void test_deleteById() {
		//ACT:
		curvePointServiceImpl.deleteById(1);
		
		//ASSERT:
		verify(curvePointRepository).deleteById(1);
	}

}
