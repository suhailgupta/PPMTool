package com.suhail.ppm.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.suhail.ppm.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import static com.suhail.ppm.security.SecurityConstants.EXPIRATION_TIME;
import static com.suhail.ppm.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {

	// Generate the Token
	public String generateToken(Authentication authentication) {

		User user = (User) authentication.getPrincipal();
		Date now = new Date(System.currentTimeMillis());

		Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
		String userId = Long.toString(user.getId());

		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("id", userId);
		claims.put("username", user.getUsername());
		claims.put("fullName", user.getFullName());

		// If we have to use roles, we have to put the roles like admin,developer also
		// in the claims.

		return Jwts.builder().setSubject(userId).setClaims(claims).setIssuedAt(now).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
	}

	// Validate the token
	public boolean validateToken(String token) {

		try {
			Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
			return true;

		} catch (SignatureException ex) {
			System.out.println("Invalid JWT SIgnature");
		} catch (MalformedJwtException ex) {
			System.out.println("Invalid JWT Token");
		} catch (ExpiredJwtException ex) {
			System.out.println("Expired JWT Token");
		} catch (UnsupportedJwtException ex) {
			System.out.println("Unsupported JWT Token");
		} catch (IllegalArgumentException ex) {
			System.out.println("JWT claims string is empty");
		}
		return false;
	}
	
	// Get user id from token
	public Long getUserIdFromJWT(String token) {
		
		Claims claims=Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		String id= (String)claims.get("id");
		return Long.parseLong(id);
	}

}
