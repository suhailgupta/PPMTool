package com.suhail.ppm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suhail.ppm.domain.User;
import com.suhail.ppm.repostories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user= userRepository.findByUsername(username);
		if(user== null) {
			throw new UsernameNotFoundException("User not found");
		}
		return user; // it returns only UserDetails.i.e. why we have our user class implements UserDetails
	}
	
	@Transactional
	public User loadUserById(Long id) {
		User user=userRepository.getById(id);
		if(user== null) {
			throw new UsernameNotFoundException("User not found");
		}
		return user;
	}

}
