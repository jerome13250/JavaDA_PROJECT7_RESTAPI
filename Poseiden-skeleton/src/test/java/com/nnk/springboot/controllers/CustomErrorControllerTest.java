package com.nnk.springboot.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;


/**
 * We cannot performing a unit test if we expect to trigger the ErrorController by calling a method from another Controller. That would be an integration test.
 * To unit test the ErrorController class, we just create an instance of the ErrorController class in a unit test and call the methods.
 * https://stackoverflow.com/questions/53889541/is-it-possible-to-use-an-errorcontroller-with-a-webmvctest/53889721#53889721
 * 
 * @author jerome
 *
 */

@ExtendWith(MockitoExtension.class)
class CustomErrorControllerTest {

	private CustomErrorController customErrorController;
	
	@Mock
	private HttpServletRequest mockHttpServletRequest;

	@Mock
	private Model mockModel;
	
	@BeforeEach
	void beforeTest()
	{
		customErrorController = new CustomErrorController();
	}

	@Test
	void handleError_404()
	{
		//ARRANGE:
		// mock the desired mockHttpServletRequest functionality.
		doReturn(404).when(mockHttpServletRequest).getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		//ACT:
		ModelAndView modelAndViewResult = customErrorController.handleError(mockHttpServletRequest, mockModel);

		// Do asserts on the actualResult here.
		assertEquals("error",modelAndViewResult.getViewName());
		verify(mockModel).addAttribute("errorMsg", "Error 404, Page not found!");
	}
	
	@Test
	void handleError_403()
	{
		//ARRANGE:
		// mock the desired mockHttpServletRequest functionality.
		doReturn(403).when(mockHttpServletRequest).getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		//ACT:
		ModelAndView modelAndViewResult = customErrorController.handleError(mockHttpServletRequest, mockModel);

		// Do asserts on the actualResult here.
		assertEquals("error",modelAndViewResult.getViewName());
		verify(mockModel).addAttribute("errorMsg", "Error 403, Access denied!");
	}
	
	@Test
	void handleError_500()
	{
		//ARRANGE:
		// mock the desired mockHttpServletRequest functionality.
		doReturn(500).when(mockHttpServletRequest).getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		//ACT:
		ModelAndView modelAndViewResult = customErrorController.handleError(mockHttpServletRequest, mockModel);

		// Do asserts on the actualResult here.
		assertEquals("error",modelAndViewResult.getViewName());
		verify(mockModel).addAttribute("errorMsg","Sorry an error has happened. Please contact our support!");
	}
	
}