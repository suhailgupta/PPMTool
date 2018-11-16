package com.suhail.ppm.repostories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.suhail.ppm.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	
}
