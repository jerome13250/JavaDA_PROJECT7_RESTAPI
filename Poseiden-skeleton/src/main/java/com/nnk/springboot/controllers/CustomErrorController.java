package com.nnk.springboot.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
	@Override
	public String getErrorPath() {
		return "/error";
	}
	
	@GetMapping("/error")
	public ModelAndView handleError(HttpServletRequest request, Model model) {
		
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		logger.debug("Error with status code " + status + " happened");
    	model.addAttribute("errorMsg", "Sorry an error has happened. Please contact our support!");
	    return new ModelAndView("error");
	}
}