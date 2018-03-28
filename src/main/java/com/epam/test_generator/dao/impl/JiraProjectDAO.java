package com.epam.test_generator.dao.impl;

import com.epam.test_generator.entities.factory.JiraClientFactory;
import com.epam.test_generator.pojo.JiraProject;
import com.epam.test_generator.services.exceptions.JiraRuntimeException;
import java.util.List;
import java.util.stream.Collectors;
import net.rcarz.jiraclient.JiraException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JiraProjectDAO {

    @Autowired
    private JiraClientFactory jiraClientFactory;

    public JiraProject getProjectByJiraKey(Long clientId, String jiraKey) {

        try {
            return new JiraProject(jiraClientFactory.getJiraClient(clientId).getProject(jiraKey));
        } catch (JiraException e) {
            throw new JiraRuntimeException(e.getMessage(), e);
        }
    }

    public List<JiraProject> getAllProjects(Long clientId) {
        try {
            return jiraClientFactory.getJiraClient(clientId).getProjects()
                .stream()
                .map(JiraProject::new)
                .collect(Collectors.toList());
        } catch (JiraException e) {
            throw new JiraRuntimeException(e.getMessage(), e);
        }
    }
}
