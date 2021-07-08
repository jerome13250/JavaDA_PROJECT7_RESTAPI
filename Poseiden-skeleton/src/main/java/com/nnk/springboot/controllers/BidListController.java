package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.services.PrincipalDataExtractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

import javax.validation.Valid;


@Controller
public class BidListController {

	Logger logger = LoggerFactory.getLogger(BidListController.class);
	
	@Autowired
	private BidListService bidListService;
	
	//@Autowired
	//PrincipalDataExtractor principalDataExtractor;
	

    @RequestMapping("/bidList/list")
    public String home(Model model, Principal principal)
    {
    	
    	//logger.info("User Name extracted : {}", principalDataExtractor.getUserName(principal));
    	
    	
    	logger.info("@RequestMapping(\"/bidList/list\")");
        model.addAttribute("listofbidlist", bidListService.findAll());
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
    	logger.info("@GetMapping(\"/bidList/add\")");
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult bindingResult, Model model) {
    	logger.info("@PostMapping(\"/bidList/validate\")");
    	//form data validation
    	if (bindingResult.hasErrors()) {        	
            return "/bidList/add";
        }
    	//save to db:
    	bidListService.save(bid);
    	//redirection do not use the current Model
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
    	logger.info("@GetMapping(\"/bidList/update/{id}\")");
    	//Get Bid by Id:
    	Optional<BidList> optbidlist = bidListService.findById(id);
    	
    	if (!optbidlist.isPresent()) {
    		model.addAttribute("errorMsg", "Sorry, this resource cannot be found.");
    		return "error";
    	}
    	
        model.addAttribute("bidList", optbidlist.get());
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                             BindingResult bindingResult, Model model) {
    	logger.info("@PostMapping(\"/bidList/update/{id}\")");
    	
    	//id validation:
    	if (Boolean.FALSE.equals(bidListService.existsById(id))) {
    		model.addAttribute("errorMsg", "Sorry, this resource cannot be found.");
    		return "error";
    	}
    	
    	//BidListId is not part of our form, so it is null in "bidList", we need to write it with "id" @PathVariable
    	bidList.setBidListId(id);
    	//form data validation:
    	if (bindingResult.hasErrors()) {        	
            return "bidList/update"; 
        }

    	bidListService.save(bidList);
    	
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteById(@PathVariable("id") Integer id, Model model) {
    	logger.info("@GetMapping(\"/bidList/delete/{id}\"");
    	
    	//id validation:
    	if (Boolean.FALSE.equals(bidListService.existsById(id))) {
    		model.addAttribute("errorMsg", "Sorry, this resource cannot be found.");
    		return "error";
    	}
    	
    	bidListService.deleteById(id);
        return "redirect:/bidList/list";
    }
}
