package com.nnk.springboot.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nnk.springboot.repositories.UserRepository;

@Controller
@RequestMapping("app")
public class LoginController {

	Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
    private UserRepository userRepository;

	/**
	 * This is an existing mapping from the original project. It redirects "app/login" to the same page as "login".
	 * @return login page
	 */
    @GetMapping("login")
    public String login() {
    	logger.info("LoginController: GET app/login");
        return "redirect:/login";
    }

    /**
     * This is an existing mapping from the original project. It maps "app/secure/article-details" to the same page as "user/list".
     * The difference is that you must be identified to access this page. 
     * 
     * @return Model with users list, View is "user/list"
     */
    @GetMapping("secure/article-details")
    public ModelAndView getAllUserArticles() {
    	logger.info("LoginController: GET app/secure/article-details");
        ModelAndView mav = new ModelAndView();
        mav.addObject("users", userRepository.findAll());
        mav.setViewName("user/list");
        return mav;
    }

    
    /**
     * This is an existing mapping from the original project. It's useless cause we have CustomErrorController implements ErrorController
     * @return
     */
    /*
    @GetMapping("error")
    public ModelAndView error() {
    	logger.error("LoginController: GET app/error");
        ModelAndView mav = new ModelAndView();
        String errorMessage= "You are not authorized for the requested data.";
        mav.addObject("errorMsg", errorMessage);
        mav.setViewName("403");
        return mav;
    }
    */

}
