package com.nnk.springboot.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;

@Controller
public class RuleNameController {
	
	@Autowired
	RuleNameService ruleNameService;

    @RequestMapping("/ruleName/list")
    public String home(Model model)
    {
    	model.addAttribute("listofRuleName", ruleNameService.findAll());
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addBidForm(RuleName bid) {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult bindingResult, Model model) {
   	
        //form data validation
    	if (bindingResult.hasErrors()) {        	
            return "/ruleName/add";
        }
    	//save to db:
    	ruleNameService.save(ruleName);
    	//note: redirection do not use the current Model
        return "redirect:/ruleName/list";

    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
   	
    	//Get RuleName by Id:
    	Optional<RuleName> optRuleName = ruleNameService.findById(id);
    	
    	if (!optRuleName.isPresent()) {
    		model.addAttribute("errorMsg", "Sorry, this resource cannot be found.");
    		return "error";
    	}
    	
        model.addAttribute("ruleName", optRuleName.get());
   	
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                             BindingResult bindingResult, Model model) {

    	//id validation:
    	if (!ruleNameService.existsById(id)) {
    		model.addAttribute("errorMsg", "Sorry, this resource cannot be found.");
    		return "error";
    	}
    	
    	//ruleName id is not part of our form, so it is null in "ruleName", we need to write it with "id" @PathVariable
    	ruleName.setId(id);
    	//form data validation:
    	if (bindingResult.hasErrors()) {        	
            return "ruleName/update"; 
        }

    	ruleNameService.save(ruleName);
    	
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
    	ruleNameService.deleteById(id);
        return "redirect:/ruleName/list";
    }
}
