package com.epam.admin_client.service;

import com.epam.admin_client.service.exception.ProjectException;
import com.epam.admin_client.service.util.HeadersBuilder;
import com.epam.test_generator.controllers.project.request.ProjectCreateDTO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class ProjectService {

    private final String uriCreateProject;
    private final String uriAdminProjects;

    private RestTemplate restTemplate;
    private HeadersBuilder headersBuilder;

    public ProjectService(Environment environment, RestTemplate restTemplate,
                          HeadersBuilder headersBuilder) {
        this.restTemplate = restTemplate;
        this.headersBuilder = headersBuilder;
        this.uriCreateProject = environment.getRequiredProperty("create.project.api.url");
        this.uriAdminProjects = environment.getRequiredProperty("admin.projects.api.url");
    }

    public ProjectDTO[] getAllProject(String token) throws IOException {
        HttpEntity<String> entity = new HttpEntity<>(headersBuilder.tokenToHeaders(token));
        ResponseEntity<ProjectDTO[]> result;
        try {
            result = restTemplate
                .exchange(uriAdminProjects, HttpMethod.GET, entity, ProjectDTO[].class);
        } catch (HttpServerErrorException e) {
            throw new ProjectException(e);
        }
        return result.getBody();
    }

    public void createProject(ProjectCreateDTO projectDTO, String token)
        throws JsonProcessingException {
        if (projectDTO != null) {
            HttpEntity<ProjectCreateDTO> entity = new HttpEntity<>(projectDTO,
                headersBuilder.tokenToHeaders(token));
            restTemplate.exchange(uriCreateProject, HttpMethod.POST, entity, String.class);
        }
    }

    public void deleteProject(ProjectDTO projectDTO, String token) {
        HttpEntity<String> entity = new HttpEntity<>(headersBuilder.tokenToHeaders(token));
        try {
            restTemplate.exchange(uriAdminProjects + "/" + projectDTO.getId(),
                HttpMethod.DELETE, entity, String.class);
        } catch (HttpStatusCodeException e) {
            throw new ProjectException(e);
        }
    }
}
