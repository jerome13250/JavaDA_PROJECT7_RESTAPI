package com.nnk.springboot.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Error Controller bean that'll replace the default one.
 * 
 * <p>
 * Baeldung: springboot <a href="https://www.baeldung.com/spring-boot-custom-error-page"> custom error-page</a>
 * </p>
 * 
 * @author jerome
 *
 */

@Controller
public class CustomErrorController implements ErrorController {
	
	Logger logger = LoggerFactory.getLogger(CustomErrorController.class);


	//starting version 2.3.x, Spring Boot has deprecated this method, and the property server.error.path should be used instead to specify the custom path.
	//But since it's still a part of the ErrorController interface and hasn't been removed entirely, we'll need to override it or else the compiler will complain
	@Override
	public String getErrorPath() {
		return "/error";
	}
	
	@RequestMapping("/error") //note: if we use @GetMapping, errors that occured on POST do not display because "Request method 'POST' not supported" ad we get a blank page...
	public ModelAndView handleError(HttpServletRequest request, Model model) {
		
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		logger.error("Error with status code {} happened", status);
    	model.addAttribute("errorMsg", "Sorry an error has happened. Please contact our support!");
	    return new ModelAndView("error");
	}
}