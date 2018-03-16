package com.epam.test_generator.dao.impl;

import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.pojo.JiraStory;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.Issue.SearchResult;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JiraStoryDAO {

    @Autowired
    private SuitDAO suitDAO;

    @Autowired
    private JiraClient client;


    private final static String TYPE = "Story";
    private final static Integer MAX_NUMBER_OF_ISSUES = Integer.MAX_VALUE;
    private final static Integer CLOSE_ACTION_ID = 31;

    public JiraStory getStoryByJiraKey(String jiraKey) throws JiraException {

        return new JiraStory(client.getIssue(jiraKey));
    }


    /**
     * Returns list of all stories from Jira that belongs to specific Jira project.
     *
     * @param jiraProjectKey
     * @return
     */
    public List<JiraStory> getStories(String jiraProjectKey) throws JiraException {

        String query = String.format("project=%s AND status in (Open, \"In Progress\", Reopened, Verified) AND type=story", jiraProjectKey);
        List<Issue> issues = client.searchIssues(query, MAX_NUMBER_OF_ISSUES).issues;
        return issues.stream().map(JiraStory::new).collect(Collectors.toList());
    }

    /**
     * Returns list of stories from Jira by specificproject which are not yet in the system.
     *
     * @param projectKey
     * @return
     */
    public List<JiraStory> getNonexistentStoriesByProject(String projectKey) throws JiraException {

        String query = String.format("project =%s AND status in (Open, \"In Progress\", Reopened, Verified) AND type=story", projectKey);
        SearchResult issues = client.searchIssues(query, MAX_NUMBER_OF_ISSUES);
        return issues.issues.stream()
                .filter(story -> suitDAO.findByJiraKey(story.getKey()) == null)
                .map(JiraStory::new)
                .collect(Collectors.toList());
    }

    public List<JiraStory> getJiraStoriesByFilter(String search) throws JiraException {

        List<Issue> issues = client.searchIssues(search, MAX_NUMBER_OF_ISSUES).issues;
        return issues.stream().map(JiraStory::new).collect(Collectors.toList());
    }

    public void updateStoryByJiraKey(Suit suit) throws JiraException {
        client
                .getIssue(suit.getJiraKey())
                .update()
                .field(Field.SUMMARY, suit.getName())
                .field(Field.DESCRIPTION, suit.getDescription())
                .execute();
        suit.setLastJiraSyncDate(LocalDateTime.now());
        suitDAO.save(suit);
    }

    public void closeStoryByJiraKey(String jiraKey) throws JiraException {

        client
                .getIssue(jiraKey)
                .transition()
                .execute(CLOSE_ACTION_ID);
    }

    public void createStory(Suit suit) throws JiraException {

        Issue issue = client
                .createIssue(suit.getJiraProjectKey(), TYPE)
                .field(Field.SUMMARY, suit.getName())
                .field(Field.DESCRIPTION, suit.getDescription())
                .execute();
        suit.setJiraKey(issue.getKey());
        suit.setJiraProjectKey(issue.getProject().getKey());
        suit.setLastJiraSyncDate(LocalDateTime.now());
        suitDAO.save(suit);
    }
}