package com.nnk.springboot.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.dto.UserFormDTO;
import com.nnk.springboot.repositories.UserRepository;

@Controller
public class UserController {
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    //https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
  	@Autowired
  	private ModelMapper modelMapper;
    
    @RequestMapping("/user/list")
    public String home(Model model)
    {
    	logger.info("@RequestMapping(\"/user/list\")");
        model.addAttribute("users", userRepository.findAll());
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(UserFormDTO userFormDTO) {
    	logger.info("@GetMapping(\"/user/add\")");
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate(@Valid UserFormDTO userFormDTO, BindingResult result, Model model) {
    	
    	logger.info("@PostMapping(\"/user/validate\")");
    	
        if (result.hasErrors()) {
        	return "user/add";
        }
        
        User user = convertToEntity(userFormDTO);
        
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/user/list";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
    	
    	logger.info("@GetMapping(\"/user/update/{id}\")");
    	
        Optional<User> optUser = userRepository.findById(id);
    	
    	if (!optUser.isPresent()) {
    		model.addAttribute("errorMsg", "Sorry, this User id cannot be found:" + id);
    		return "error";
    	}
        
        UserFormDTO userFormDTO = convertToDTO(optUser.get());
        model.addAttribute("userFormDTO", userFormDTO);
        return "user/update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid UserFormDTO userFormDTO,
                             BindingResult result, Model model) {
    	
    	logger.info("@PostMapping(\"/user/update/{id}\")");
    	
    	//id validation:
    	if (!userRepository.existsById(id)) {
    		model.addAttribute("errorMsg", "Sorry, this User id cannot be found:" + id);
    		return "error";
    	}
    	
    	//user id is not part of our form, so it is null in "userFormDTO", we need to write it with "id" @PathVariable
    	userFormDTO.setId(id);
    	
    	if (result.hasErrors()) {
            return "user/update";
        }
        
        User user = convertToEntity(userFormDTO);
        
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        
    	logger.info("@GetMapping(\"/user/delete/{id}\")");
    	
    	Boolean existUser = userRepository.existsById(id);
        
        if(Boolean.FALSE.equals(existUser)) {
        	model.addAttribute("errorMsg", "Sorry, this User id cannot be found:" + id);
    		return "error";
        }
        
        userRepository.deleteById(id);
        return "redirect:/user/list";
    }
    
    /**
     * This method converts a DTO object to an Entity
     * 
     * @param userFormDTO
     * @return Entity version of the DTO
     * 
     * @see <a href="https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application"> Entity/DTO conversion
     */
    private User convertToEntity(UserFormDTO userFormDTO) {
    	User user = modelMapper.map(userFormDTO, User.class);

        return user;
    }
    
    /**
     * This method converts an Entity to a DTO object, this resets password.
     * 
     * @param user entity
     * @return DTO version of the Entity with password set to ""
     * 
     * @see <a href="https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application"> Entity/DTO conversion
     */
    private UserFormDTO convertToDTO(User user) {
    	UserFormDTO userFormDTO = modelMapper.map(user, UserFormDTO.class);
    	userFormDTO.setPassword("");
        return userFormDTO;
    }
    
    
}
