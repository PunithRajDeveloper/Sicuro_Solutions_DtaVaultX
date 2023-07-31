package com.prc.sicuro.springbootApp.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.prc.sicuro.springbootApp.model.User;
import com.prc.sicuro.springbootApp.repository.UserRepository;
import com.prc.sicuro.springbootApp.service.UserService;
import com.prc.sicuro.springbootApp.web.dto.UserRegistrationDto;

@SuppressWarnings("unused")
@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

	private UserService userService;
	
	@Autowired
	private UserRepository userRepo;
    
	@Autowired
	public UserRegistrationController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }
	
	@GetMapping
	public String showRegistrationForm() {
		return "registration";
	}
	
	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto, HttpSession model) {
		User user = userRepo.findByEmail(registrationDto.getEmail()); 
		
		if (user == null) {
            userService.save(registrationDto);
            return "redirect:/registration?success"; 
        } 
		
		else 
            model.setAttribute("error", "User already exists. Please use a different email Or login with the existing account.");
            return "redirect:/registration?error"; 
	}
}
