package com.suhail.ppm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suhail.ppm.domain.Project;
import com.suhail.ppm.exceptions.ProjectIdException;
import com.suhail.ppm.repostories.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	public Project saveOrUpdateProject(Project project) {

		try {
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			return projectRepository.save(project);
		} catch (Exception e) {
			throw new ProjectIdException(
					"Project ID '" + project.getProjectIdentifier().toUpperCase() + "' already exists");
		}

	}

	public Project findProjectByIdentifier(String projectId) {

		Project project = projectRepository.findByProjectIdentifier(projectId);
		if (project == null) {
			throw new ProjectIdException("Project ID '" + projectId.toUpperCase() + "' does not exists");
		}
		return project;
	}

	public Iterable<Project> findAllProjects() {
		return projectRepository.findAll();

	}

	public void deleteProjectByIdentifier(String projectId) {
		Project project = projectRepository.findByProjectIdentifier(projectId);
		if (project == null) {
			throw new ProjectIdException(
					"Cannot delete Project ID '" + projectId.toUpperCase() + "' .Projectdoes not exists");
		}
		
		projectRepository.delete(project);
	}

}
