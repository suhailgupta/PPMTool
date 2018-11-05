package com.suhail.ppm.repostories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.suhail.ppm.domain.Backlog;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog, Long>{
	

	 Backlog findByProjectIdentifier(String identifier);
}
