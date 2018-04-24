package com.epam.test_generator.dao.impl;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.factory.JiraClientFactory;
import com.epam.test_generator.pojo.JiraSubTask;
import com.epam.test_generator.services.exceptions.JiraRuntimeException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.RestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JiraSubStoryDAO {

    @Autowired
    private CaseDAO caseDAO;

    @Autowired
    private JiraClientFactory jiraClientFactory;

    private final static String TYPE = "Sub-story";
    private final static Integer MAX_NUMBER_OF_ISSUES = 10000;

    public JiraSubTask getSubStoryByJiraKey(Long clientId, String jiraKey) throws JiraException {

        return new JiraSubTask(jiraClientFactory.getJiraClient(clientId).getIssue(jiraKey));
    }

    public List<JiraSubTask> getJiraSubtoriesByFilter(Long clientId, String search) {
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
            throw new JiraRuntimeException(e.getMessage(), e);
        }
    }

    public void updateSubStoryByJiraKey(Long clientId, Case caze) {
        try {
            jiraClientFactory.getJiraClient(clientId)
                .getIssue(caze.getJiraKey())
                .update()
                .field(Field.SUMMARY, caze.getName())
                .field(Field.DESCRIPTION, caze.getDescription())
                .execute();
            caze.setLastJiraSyncDate(ZonedDateTime.now());
            caseDAO.save(caze);
        } catch (JiraException e) {
            throw new JiraRuntimeException(e.getMessage(), e);
        }

    }

    public void createSubStory(Long clientId, Case caze) {
        try {
            Issue execute = jiraClientFactory.getJiraClient(clientId)
                .createIssue(caze.getJiraProjectKey(), TYPE)
                .field(Field.SUMMARY, caze.getName())
                .field(Field.DESCRIPTION, caze.getDescription())
                .field(Field.PARENT, caze.getJiraParentKey())
                .execute();
            caze.setJiraKey(execute.getKey());
            caze.setJiraProjectKey(execute.getProject().getKey());
            caze.setJiraParentKey(execute.getParent().getKey());
            caze.setLastJiraSyncDate(ZonedDateTime.now());
            caseDAO.save(caze);
        } catch (JiraException e) {
            throw new JiraRuntimeException(e.getMessage(), e);
        }

    }


}
