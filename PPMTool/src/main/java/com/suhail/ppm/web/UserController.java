package com.suhail.ppm.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suhail.ppm.domain.User;
import com.suhail.ppm.payload.JWTLoginSuccessResponse;
import com.suhail.ppm.payload.LoginRequest;
import com.suhail.ppm.security.JwtTokenProvider;
import com.suhail.ppm.security.SecurityConstants;
import com.suhail.ppm.services.MapValidationErrorService;
import com.suhail.ppm.services.UserService;
import com.suhail.ppm.validator.UserValidator;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@Autowired
	private UserService userService;
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private JwtTokenProvider tokenProvider;
	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {
		// Validate passwords match
		userValidator.validate(user, result);
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		if (errorMap != null)
			return errorMap;

		User newUser = userService.saveUser(user);
		return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
		
		ResponseEntity<?> errorMap=mapValidationErrorService.MapValidationService(result);
		 if(errorMap != null) return errorMap;
		 
		 Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
	
		 SecurityContextHolder.getContext().setAuthentication(authentication);
		 String jwt=SecurityConstants.TOKEN_PREFIX+tokenProvider.generateToken(authentication);
		 return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
	
	}
}
