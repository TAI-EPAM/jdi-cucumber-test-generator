package com.epam.test_generator.dao.impl;

import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.factory.JiraClientFactory;
import com.epam.test_generator.pojo.JiraStory;
import com.epam.test_generator.services.exceptions.JiraRuntimeException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JiraStoryDAO {

    @Autowired
    private SuitDAO suitDAO;

    @Autowired
    private JiraClientFactory jiraClientFactory;

    private final static String TYPE = "Story";
    private final static Integer MAX_NUMBER_OF_ISSUES = Integer.MAX_VALUE;

    public JiraStory getStoryByJiraKey(Long clientId, String jiraKey) throws JiraException {

        return new JiraStory(jiraClientFactory.getJiraClient(clientId).getIssue(jiraKey));
    }


    /**
     * Returns list of all stories from Jira that belongs to specific Jira project.
     */
    public List<JiraStory> getStories(Long clientId, String jiraProjectKey) throws JiraException {

        String query = String.format(
            "project=%s AND status in (Open, \"In Progress\", Reopened, Verified) AND type=story",
            jiraProjectKey);
        List<Issue> issues = jiraClientFactory.getJiraClient(clientId)
            .searchIssues(query, MAX_NUMBER_OF_ISSUES).issues;
        return mapIssuesToJiraStories(issues);
    }

    /**
     * Returns list of stories from Jira by specificproject which are not yet in the system.
     */
    public List<JiraStory> getNonexistentStoriesByProject(Long clientId, String jiraProjectKey) {
        try {
            return getStories(clientId, jiraProjectKey).stream()
                .filter(story -> suitDAO.findByJiraKey(story.getJiraKey()) == null)
                .collect(Collectors.toList());
        } catch (JiraException e) {
            throw new JiraRuntimeException(e.getMessage(), e);
        }
    }

    public List<JiraStory> getJiraStoriesByFilter(Long clientId, String search) {

        List<Issue> issues;
        try {
            issues = jiraClientFactory.getJiraClient(clientId)
                .searchIssues(search, MAX_NUMBER_OF_ISSUES).issues;
        } catch (JiraException e) {
            throw new JiraRuntimeException(e.getMessage(), e);
        }
        return mapIssuesToJiraStories(issues);
    }

    public void updateStoryByJiraKey(Long clientId, Suit suit) {
        try {
            jiraClientFactory.getJiraClient(clientId)
                .getIssue(suit.getJiraKey())
                .update()
                .field(Field.SUMMARY, suit.getName())
                .field(Field.DESCRIPTION, suit.getDescription())
                .execute();
        } catch (JiraException e) {
            throw new JiraRuntimeException(e.getMessage(), e);
        }
        suit.setLastJiraSyncDate(ZonedDateTime.now());
        suitDAO.save(suit);
    }


    public void changeStatusByJiraKey(Long clientId, String jiraKey, Integer actionId) {
        if (actionId == null) {
            return;
        }
        try {
            jiraClientFactory.getJiraClient(clientId)
                .getIssue(jiraKey)
                .transition()
                .execute(actionId);
        } catch (JiraException e) {
            throw new JiraRuntimeException(e.getMessage(), e);
        }
    }

    public void createStory(Long clientId, Suit suit) {

        try {
            Issue issue = jiraClientFactory.getJiraClient(clientId)
                .createIssue(suit.getJiraProjectKey(), TYPE)
                .field(Field.SUMMARY, suit.getName())
                .field(Field.DESCRIPTION, suit.getDescription())
                .execute();
            suit.setJiraKey(issue.getKey());
            suit.setJiraProjectKey(issue.getProject().getKey());
            suit.setLastJiraSyncDate(ZonedDateTime.now());
            suitDAO.save(suit);
        } catch (JiraException e) {
            throw new JiraRuntimeException(e.getMessage(), e);
        }
    }


    private List<JiraStory> mapIssuesToJiraStories(List<Issue> issues) {
        return issues
            .stream()
            .filter(s -> !s.getIssueType().isSubtask())
            .map(JiraStory::new)
            .collect(Collectors.toList());
    }
}