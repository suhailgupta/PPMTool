package com.suhail.ppm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suhail.ppm.domain.Backlog;
import com.suhail.ppm.domain.Project;
import com.suhail.ppm.domain.User;
import com.suhail.ppm.exceptions.ProjectIdException;
import com.suhail.ppm.exceptions.ProjectNotFoundException;
import com.suhail.ppm.repostories.BacklogRepository;
import com.suhail.ppm.repostories.ProjectRepository;
import com.suhail.ppm.repostories.UserRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private BacklogRepository backlogRepository;
	@Autowired
	private UserRepository userRepository;

	public Project saveOrUpdateProject(Project project, String username) {

		String projectIdentifier = project.getProjectIdentifier().toUpperCase();
		try {
			User user= userRepository.findByUsername(username);
			project.setUser(user);
			project.setProjectLeader(user.getUsername());
			project.setProjectIdentifier(projectIdentifier);
			
			if(project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(projectIdentifier);
			}
			else {
				project.setBacklog(backlogRepository.findByProjectIdentifier(projectIdentifier));
			}
			
			return projectRepository.save(project);
		} catch (Exception e) {
			throw new ProjectIdException(
					"Project ID '" + projectIdentifier + "' already exists");
		}

	}

	public Project findProjectByIdentifier(String projectId, String username) {

		//Only want to return the project if the user looking for it is the Owner.
		
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		if (project == null) {
			throw new ProjectIdException("Project ID '" + projectId.toUpperCase() + "' does not exists");
		}
		
		if(!project.getProjectLeader().equals(username)) {
			throw new ProjectNotFoundException("Project not found in your account");
		}
		return project;
	}

	public Iterable<Project> findAllProjects(String username) {
		return projectRepository.findAllByProjectLeader(username);

	}

	public void deleteProjectByIdentifier(String projectId, String username) {
		
		projectRepository.delete(findProjectByIdentifier(projectId, username));
	}

}
