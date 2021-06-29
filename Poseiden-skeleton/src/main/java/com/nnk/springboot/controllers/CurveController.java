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

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;

@Controller
public class CurveController {
	
	@Autowired
	CurvePointService curvePointService;

    @RequestMapping("/curvePoint/list")
    public String home(Model model)
    {
    	model.addAttribute("listofCurvePoint", curvePointService.findAll());
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addBidForm(CurvePoint bid) {
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult bindingResult, Model model) {
   	
        //form data validation
    	if (bindingResult.hasErrors()) {        	
            return "/curvePoint/add";
        }
    	//save to db:
    	curvePointService.save(curvePoint);
    	//note: redirection do not use the current Model
        return "redirect:/curvePoint/list";

    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
   	
    	//Get CurvePoint by Id:
    	Optional<CurvePoint> optCurvePoint = curvePointService.findById(id);
    	
    	if (!optCurvePoint.isPresent()) {
    		model.addAttribute("errorMsg", "Sorry, this resource cannot be found.");
    		return "error";
    	}
    	
        model.addAttribute("curvePoint", optCurvePoint.get());
   	
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                             BindingResult bindingResult, Model model) {

    	//id validation:
    	if (!curvePointService.existsById(id)) {
    		model.addAttribute("errorMsg", "Sorry, this resource cannot be found.");
    		return "error";
    	}
    	
    	//curvePoint id is not part of our form, so it is null in "curvePoint", we need to write it with "id" @PathVariable
    	curvePoint.setId(id);
    	//form data validation:
    	if (bindingResult.hasErrors()) {        	
            return "curvePoint/update"; 
        }

    	curvePointService.save(curvePoint);
    	
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
    	curvePointService.deleteById(id);
        return "redirect:/curvePoint/list";
    }
}
