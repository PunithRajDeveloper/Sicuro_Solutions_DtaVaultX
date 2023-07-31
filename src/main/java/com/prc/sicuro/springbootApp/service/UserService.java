package com.prc.sicuro.springbootApp.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.prc.sicuro.springbootApp.model.User;
import com.prc.sicuro.springbootApp.web.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService {
	User save(UserRegistrationDto registrationDto);

	User updateUser(User user);

	void deleteUser(Long userId);
}
