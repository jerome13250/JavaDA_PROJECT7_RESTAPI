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

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;

@Controller
public class TradeController {
    
	@Autowired
	TradeService tradeService;

    @RequestMapping("/trade/list")
    public String home(Model model)
    {
    	model.addAttribute("listofTrade", tradeService.findAll());
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addBidForm(Trade bid) {
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult bindingResult, Model model) {
   	
        //form data validation
    	if (bindingResult.hasErrors()) {        	
            return "/trade/add";
        }
    	//save to db:
    	tradeService.save(trade);
    	//note: redirection do not use the current Model
        return "redirect:/trade/list";

    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
   	
    	//Get Trade by Id:
    	Optional<Trade> optTrade = tradeService.findById(id);
    	
    	if (!optTrade.isPresent()) {
    		model.addAttribute("errorMsg", "Sorry, this resource cannot be found.");
    		return "error";
    	}
    	
        model.addAttribute("trade", optTrade.get());
   	
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid Trade trade,
                             BindingResult bindingResult, Model model) {

    	//id validation:
    	if (!tradeService.existsById(id)) {
    		model.addAttribute("errorMsg", "Sorry, this resource cannot be found.");
    		return "error";
    	}
    	
    	//trade id is not part of our form, so it is null in "trade", we need to write it with "id" @PathVariable
    	trade.setTradeId(id);
    	//form data validation:
    	if (bindingResult.hasErrors()) {        	
            return "trade/update"; 
        }

    	tradeService.save(trade);
    	
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
    	 Boolean existTrade = tradeService.existsById(id);
         
         if(Boolean.FALSE.equals(existTrade)) {
         	model.addAttribute("errorMsg", "Sorry, this Trade id cannot be found:" + id);
     		return "error";
         }
    	
    	tradeService.deleteById(id);
        return "redirect:/trade/list";
    }
}
