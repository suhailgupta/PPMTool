package com.suhail.ppm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suhail.ppm.domain.Backlog;
import com.suhail.ppm.domain.Project;
import com.suhail.ppm.domain.ProjectTask;
import com.suhail.ppm.exceptions.ProjectNotFoundException;
import com.suhail.ppm.repostories.BacklogRepository;
import com.suhail.ppm.repostories.ProjectRepository;
import com.suhail.ppm.repostories.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private BacklogRepository backlogRepository;
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	@Autowired
	private ProjectRepository projectRepository;

	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

		// Exceptions Project not found
		/*
		 * { ProjectNotFound: "Project Not Found" }
		 */
		try {

			// PTs to be added to a specific project, project != null, BL exists
			Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
			// set the backlog to the project task
			projectTask.setBacklog(backlog);
			// we want our project sequence to be IDPRO-1 , IDPRO-2 ...
			// If we delete IDPRO-2, the sequence shall start from IDPRO-3
			Integer backlogSequence = backlog.getPTSequence();

			// Before we persist the Task, we should update the BL sequence
			backlogSequence++;
			backlog.setPTSequence(backlogSequence);
			// Add Sequence to the PRoject Task
			projectTask.setProjectSequence(projectIdentifier + "--" + backlogSequence);
			projectTask.setProjectIdentifier(projectIdentifier);
			// INITIAL Priority when priority null
			if (projectTask.getPriority() == 0 || projectTask.getPriority() == null) {
				projectTask.setPriority(3);
			}
			// Initial Status when status is null
			if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
				projectTask.setStatus("TO_DO");
			}
			return projectTaskRepository.save(projectTask);
		} catch (Exception ex) {
			throw new ProjectNotFoundException("Project Not Found");
		}
	}

	public Iterable<ProjectTask> findBacklogById(String id) {

		Project project = projectRepository.findByProjectIdentifier(id);
		if (project == null) {
			throw new ProjectNotFoundException("Project with id: '" + id + "' does not exists");
		}
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}

	public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id) {

		// make sure we are searching on the existing backlog
		Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
		if (backlog == null) {
			throw new ProjectNotFoundException("Project with id: '" + backlog_id + "' does not exists");
		}
		// make sure that our task exists
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		if (projectTask == null) {
			throw new ProjectNotFoundException("Project Task '" + pt_id + "' not found");
		}

		// make sure that backlog/project id in the path corresponds to the right
		// project
		if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException(
					"Project Task '" + pt_id + "' does not exists in project '" + backlog_id + "'");
			// localhost:8080/api/backlog/ID02/ID02-1.
			// it checks the ID02 matches with the passed backlog_id (project id)
		}

		return projectTask;
	}

	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id) {

		// Update project task
		// find existing project task
//		ProjectTask projectTask=projectTaskRepository.findByProjectSequence(pt_id);
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
		// replace it with updated task
		projectTask = updatedTask;
		// save update
		return projectTaskRepository.save(projectTask);
	}

	public void deletePTByProjectSequence(String backlog_id, String pt_id) {

		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);

		/*
		 * Backlog backlog = projectTask.getBacklog(); List<ProjectTask> projectTasks =
		 * backlog.getProjectTasks(); projectTasks.remove(projectTask);
		 * backlogRepository.save(backlog);
		 */

		projectTaskRepository.delete(projectTask);
	}
}
