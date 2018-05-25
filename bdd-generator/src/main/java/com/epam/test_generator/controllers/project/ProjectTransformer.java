package com.epam.test_generator.controllers.project;

import com.epam.test_generator.controllers.project.request.ProjectCreateDTO;
import com.epam.test_generator.controllers.project.request.ProjectUpdateDTO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.project.response.ProjectFullDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.controllers.user.UserDTOsTransformer;
import com.epam.test_generator.controllers.user.response.UserDTO;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.controllers.suit.SuitTransformer;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectTransformer {

    @Autowired
    private UserDTOsTransformer userTransformer;

    @Autowired
    private SuitTransformer suitTransformer;

    public Project fromDto(ProjectCreateDTO projectDTO) {
        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        return project;
    }

    public ProjectDTO toDto(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setName(project.getName());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setJiraKey(project.getJiraKey());
        projectDTO.setActive(project.isActive());
        if (project.getUsers() != null) {
            Set<UserDTO> outputUsers = project.getUsers().stream()
                .map(userTransformer::toUserDTO).collect(Collectors.toSet());
            projectDTO.setUsers(outputUsers);
        }
        return projectDTO;
    }

    public ProjectFullDTO toFullDto(Project project) {
        ProjectFullDTO projectFullDTO = new ProjectFullDTO();
        projectFullDTO.setId(project.getId());
        projectFullDTO.setName(project.getName());
        projectFullDTO.setDescription(project.getDescription());
        projectFullDTO.setJiraKey(project.getJiraKey());

        if (project.getUsers() != null) {
            Set<UserDTO> outputUsers = project.getUsers().stream()
                .map(userTransformer::toUserDTO).collect(Collectors.toSet());
            projectFullDTO.setUsers(outputUsers);
        }
        // should be changed on our suitTransformer
        if (project.getSuits() != null) {
            List<SuitDTO> outputSuits = project.getSuits().stream()
                .map(suitTransformer::toDto).collect(Collectors.toList());
            projectFullDTO.setSuits(outputSuits);
        }
        projectFullDTO.setActive(project.isActive());
        return projectFullDTO;
    }

    public Project updateFromDto(ProjectUpdateDTO projectUpdateDTO, Project project) {
        if (projectUpdateDTO.getName() != null) {
            project.setName(projectUpdateDTO.getName());
        }
        if (projectUpdateDTO.getDescription() != null) {
            project.setDescription(projectUpdateDTO.getDescription());
        }
        return project;
    }

    public List<ProjectDTO> toDtoList(List<Project> projects) {
        return projects.stream().map(this::toDto).collect(Collectors.toList());
    }

}
