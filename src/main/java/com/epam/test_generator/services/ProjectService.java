package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;
import static com.epam.test_generator.services.utils.UtilsService.checkProjectIsActive;
import static com.epam.test_generator.services.utils.UtilsService.userBelongsToProject;

import com.epam.test_generator.config.security.AuthenticatedUser;
import com.epam.test_generator.dao.interfaces.ProjectDAO;
import com.epam.test_generator.dto.ProjectDTO;
import com.epam.test_generator.dto.ProjectFullDTO;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.transformers.ProjectFullTransformer;
import com.epam.test_generator.transformers.ProjectTransformer;
import com.google.common.collect.Sets;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private ProjectTransformer projectTransformer;

    @Autowired
    private ProjectFullTransformer projectFullTransformer;

    @Autowired
    private UserService userService;

    public List<ProjectDTO> getProjects() {
        return projectTransformer.toDtoList(projectDAO.findAll());
    }

    /**
     * Returns list of {@link Project} to which current user has been assigned.
     * @param authentication {@link Authentication} objects that contains information
     * about current authorized user.
     * @return list of {@link Project} to which current user has been assigned.
     */
    public List<ProjectDTO> getAuthenticatedUserProjects(Authentication authentication) {
        AuthenticatedUser userDetails = (AuthenticatedUser) authentication.getPrincipal();
        return projectTransformer.toDtoList(getProjectsByUserId(userDetails.getId()));
    }

    public List<Project> getProjectsByUserId(Long userId) {
        User user = userService.getUserById(userId);
        checkNotNull(user);
        return projectDAO.findByUsers(user);
    }

    public Project getProjectByProjectId(Long projectId) {
        Project project = projectDAO.findOne(projectId);
        checkNotNull(project);

        return project;
    }

    public ProjectFullDTO getAuthUserFullProject(Long projectId, Authentication authentication) {
        AuthenticatedUser userDetails = (AuthenticatedUser) authentication.getPrincipal();
        User user = userService.getUserByEmail(userDetails.getEmail());
        Project project = getProjectByProjectId(projectId);

        userBelongsToProject(project, user);
        return projectFullTransformer.toDto(project);
    }

    public Long createProject(ProjectDTO projectDTO, Authentication authentication) {
        AuthenticatedUser userDetails = (AuthenticatedUser) authentication.getPrincipal();
        User authUser = userService.getUserByEmail(userDetails.getEmail());

        Project project = projectTransformer.fromDto(projectDTO);
        project.addUser(authUser);
        project.setActive(true);

        project = projectDAO.save(project);

        return project.getId();
    }

    public void updateProject(Long projectId, ProjectDTO projectDTO) {
        Project project = projectDAO.findOne(projectId);
        checkNotNull(project);
        checkProjectIsActive(project);

        projectTransformer.mapDTOToEntity(projectDTO, project);
        project.setId(projectId);
        projectDAO.save(project);
    }

    public void removeProject(Long projectId) {
        Project project = projectDAO.findOne(projectId);
        checkNotNull(project);
        projectDAO.delete(projectId);
    }

    public void addUserToProject(long projectId, long userId) {
        Project project = projectDAO.findOne(projectId);
        checkNotNull(project);
        checkProjectIsActive(project);
        User user = userService.getUserById(userId);
        checkNotNull(user);

        project.getUsers().add(user);
        projectDAO.save(project);
    }

    public void removeUserFromProject(long projectId, long userId) {
        Project project = projectDAO.findOne(projectId);
        checkNotNull(project);
        checkProjectIsActive(project);
        User user = userService.getUserById(userId);
        checkNotNull(user);

        project.getUsers().remove(user);
        projectDAO.save(project);
    }

    public void closeProject(long projectId) {
        Project project = projectDAO.findOne(projectId);
        checkNotNull(project);
        checkProjectIsActive(project);
        project.setActive(false);
        projectDAO.save(project);
    }
}
