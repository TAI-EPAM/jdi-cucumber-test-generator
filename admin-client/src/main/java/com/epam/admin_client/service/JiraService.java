package com.epam.admin_client.service;

import com.epam.admin_client.service.exception.JiraException;
import com.epam.admin_client.service.util.HeadersBuilder;
import com.epam.test_generator.controllers.admin.request.JiraSettingsCreateDTO;
import com.epam.test_generator.controllers.admin.response.JiraSettingsDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class JiraService {


    private final String jiraApiUrl;

    private RestTemplate restTemplate;
    private HeadersBuilder headersBuilder;

    public JiraService(Environment environment, RestTemplate restTemplate,
                       HeadersBuilder headersBuilder) {
        this.restTemplate = restTemplate;
        this.headersBuilder = headersBuilder;
        this.jiraApiUrl = environment.getRequiredProperty("jira.api.url");
    }

    public JiraSettingsDTO[] getJiraSettings(String token) throws IOException {
        HttpEntity<String> entity = new HttpEntity<>(headersBuilder.tokenToHeaders(token));
        ResponseEntity<JiraSettingsDTO[]> result;

        try {
            result = restTemplate
                .exchange(jiraApiUrl, HttpMethod.GET, entity, JiraSettingsDTO[].class);
        } catch (HttpStatusCodeException e) {
            throw new JiraException(e);
        }
        return result.getBody();
    }

    public void setJiraSettings(JiraSettingsCreateDTO jiraDTO, String token)
        throws JsonProcessingException {
        if (jiraDTO != null) {
            HttpEntity<JiraSettingsCreateDTO> entity = new HttpEntity<>(jiraDTO,
                headersBuilder.tokenToHeaders(token));
            restTemplate
                .exchange(jiraApiUrl, HttpMethod.POST, entity, JiraSettingsCreateDTO.class);
        }
    }

}
