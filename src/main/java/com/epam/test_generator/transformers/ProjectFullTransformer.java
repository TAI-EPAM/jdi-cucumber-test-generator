package com.epam.test_generator.transformers;

import com.epam.test_generator.dto.ProjectDTO;
import com.epam.test_generator.dto.ProjectFullDTO;
import com.epam.test_generator.entities.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectFullTransformer extends AbstractDozerTransformer<Project, ProjectFullDTO> {

    @Override
    protected Class<Project> getEntityClass() {
        return Project.class;
    }

    @Override
    protected Class<ProjectFullDTO> getDTOClass() {
        return ProjectFullDTO.class;
    }
}
