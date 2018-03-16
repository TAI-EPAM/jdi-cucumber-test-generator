package com.epam.test_generator.dao.impl;

import com.epam.test_generator.pojo.JiraProject;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JiraProjectDAO {

    @Autowired
    private JiraClient client;

    public JiraProject getProjectByJiraKey(String jiraKey) throws JiraException {

        return new JiraProject(client.getProject(jiraKey));
    }

    public List<JiraProject> getAllProjects() throws JiraException {

        return client.getProjects().stream().map(JiraProject::new).collect(Collectors.toList());
    }
}
