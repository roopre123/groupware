package com.jun.service;

import org.springframework.stereotype.Service;

import com.jun.model.User;
import com.jun.repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User join(User user) {
		User userRe = userRepository.save(user);
		return userRe;
	}
	
	public Boolean existsByCode(String code) {
		return userRepository.existsByCode(code);
	}
	
	public User findCodeByUsername(String username) {
		return userRepository.findCodeByUsername(username);
	}
	
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	
	
	
}
