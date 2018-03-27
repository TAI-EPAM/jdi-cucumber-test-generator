package com.epam.test_generator.dao.impl;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.factory.JiraClientFactory;
import com.epam.test_generator.pojo.JiraSubTask;
import net.rcarz.jiraclient.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JiraSubStroryDAO {

    @Autowired
    private CaseDAO caseDAO;

    @Autowired
    private JiraClientFactory jiraClientFactory;

    private final static String TYPE = "Sub-story";
    private final static Integer MAX_NUMBER_OF_ISSUES = 10000;

    public JiraSubTask getSubStoryByJiraKey(Long clientId, String jiraKey) throws JiraException {

        return new JiraSubTask(jiraClientFactory.getJiraClient(clientId).getIssue(jiraKey));
    }

    public List<JiraSubTask> getJiraSubtoriesByFilter(Long clientId, String search)
        throws JiraException {
        List<JiraSubTask> jiraStories = new ArrayList<>();

        try {
            List<Issue> issues = jiraClientFactory.getJiraClient(clientId)
                .searchIssues(search, MAX_NUMBER_OF_ISSUES).issues;
            return issues.stream().map(JiraSubTask::new).collect(Collectors.toList());

        } catch (JiraException e) {
            if (e.getCause() instanceof RestException) {
                RestException restException = (RestException) e.getCause();
                if (restException.getHttpStatusCode() == HttpStatus.NOT_FOUND.value()) {
                    return Collections.emptyList();
                }
            }
            throw e;
        }
    }

    public void updateSubStoryByJiraKey(Long clientId, Case caze) throws JiraException {
        jiraClientFactory.getJiraClient(clientId)
            .getIssue(caze.getJiraKey())
            .update()
            .field(Field.SUMMARY, caze.getName())
            .field(Field.DESCRIPTION, caze.getDescription())
            .execute();
        caze.setLastJiraSyncDate(LocalDateTime.now());
        caseDAO.save(caze);

    }


    public void createSubStory(Long clientId, Case caze) throws JiraException {

        Issue execute = jiraClientFactory.getJiraClient(clientId)
            .createIssue(caze.getJiraProjectKey(), TYPE)
            .field(Field.SUMMARY, caze.getName())
            .field(Field.DESCRIPTION, caze.getDescription())
            .field(Field.PARENT, caze.getJiraParentKey())
            .execute();
        caze.setJiraKey(execute.getKey());
        caze.setJiraProjectKey(execute.getProject().getKey());
        caze.setJiraParentKey(execute.getParent().getKey());
        caze.setLastJiraSyncDate(LocalDateTime.now());
        caseDAO.save(caze);

    }


}
