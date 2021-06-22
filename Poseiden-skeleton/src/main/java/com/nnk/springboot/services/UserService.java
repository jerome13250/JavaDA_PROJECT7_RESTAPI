package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;

public interface UserService {

	User findByUsername(String username);
	
}
