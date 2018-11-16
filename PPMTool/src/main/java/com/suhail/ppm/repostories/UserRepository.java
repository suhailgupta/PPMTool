package com.suhail.ppm.repostories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.suhail.ppm.domain.User;
import java.lang.String;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	User findByUsername(String username);
	
	User getById(Long id);
}
