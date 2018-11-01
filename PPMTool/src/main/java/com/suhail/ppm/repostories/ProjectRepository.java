package com.suhail.ppm.repostories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.suhail.ppm.domain.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

	@Override
	Iterable<Project> findAllById(Iterable<Long> ids);

	@Override
	Iterable<Project> findAll();

	Project findByProjectIdentifier(String projectidentifier);
}
