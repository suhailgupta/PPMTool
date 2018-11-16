package com.suhail.ppm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.suhail.ppm.domain.User;
import com.suhail.ppm.exceptions.UsernameAlreadyExistsException;
import com.suhail.ppm.repostories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveUser(User newUser) {
		try {
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			// Username has to be unique
			newUser.setUsername(newUser.getUsername()); // it will throw exception because we have set username to be unique at entity level.
			//Make sure that Password and confirm Password match
			// we dont persist or show the Confirm Password.
			newUser.setConfirmPassword("");
			return userRepository.save(newUser);
			
		} catch (Exception e) {
			throw new UsernameAlreadyExistsException("Username '"+newUser.getUsername()+"' already exists.");
		}
	}

}
