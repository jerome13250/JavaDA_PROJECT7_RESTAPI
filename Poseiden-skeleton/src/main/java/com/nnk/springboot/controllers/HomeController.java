package com.nnk.springboot.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController
{
	
	Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping("/")
	public String home(Model model)
	{
		return "home";
	}

	/**
	 * This is an existing mapping from the original project. It maps "/admin/home" to the same page as "bidList/list".
     * The difference is that you must have "ADMIN" role to access. 
     * 
	 * @param model, Spring classic Model
	 * @return view to bidList/list
	 */
	@RequestMapping("/admin/home")
	public String adminHome(Model model)
	{
		logger.info("@RequestMapping(\"/admin/home\")");
		return "redirect:/bidList/list";
	}


}
