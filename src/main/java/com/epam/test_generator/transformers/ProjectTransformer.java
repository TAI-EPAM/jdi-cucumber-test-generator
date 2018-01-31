package com.epam.test_generator.transformers;

import com.epam.test_generator.dto.ProjectDTO;
import com.epam.test_generator.entities.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectTransformer extends AbstractDozerTransformer<Project, ProjectDTO> {

    @Override
    protected Class<Project> getEntityClass() {
        return Project.class;
    }

    @Override
    protected Class<ProjectDTO> getDTOClass() {
        return ProjectDTO.class;
    }



}
