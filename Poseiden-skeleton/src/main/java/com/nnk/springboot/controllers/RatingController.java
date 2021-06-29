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

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;

@Controller
public class RatingController {
	
	@Autowired
	RatingService ratingService;

    @RequestMapping("/rating/list")
    public String home(Model model)
    {
    	model.addAttribute("listofRating", ratingService.findAll());
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addBidForm(Rating bid) {
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult bindingResult, Model model) {
   	
        //form data validation
    	if (bindingResult.hasErrors()) {        	
            return "/rating/add";
        }
    	//save to db:
    	ratingService.save(rating);
    	//note: redirection do not use the current Model
        return "redirect:/rating/list";

    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
   	
    	//Get Rating by Id:
    	Optional<Rating> optRating = ratingService.findById(id);
    	
    	if (!optRating.isPresent()) {
    		model.addAttribute("errorMsg", "Sorry, this resource cannot be found.");
    		return "error";
    	}
    	
        model.addAttribute("rating", optRating.get());
   	
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult bindingResult, Model model) {

    	//id validation:
    	if (!ratingService.existsById(id)) {
    		model.addAttribute("errorMsg", "Sorry, this resource cannot be found.");
    		return "error";
    	}
    	
    	//rating id is not part of our form, so it is null in "rating", we need to write it with "id" @PathVariable
    	rating.setId(id);
    	//form data validation:
    	if (bindingResult.hasErrors()) {        	
            return "rating/update"; 
        }

    	ratingService.save(rating);
    	
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
    	ratingService.deleteById(id);
        return "redirect:/rating/list";
    }
}
