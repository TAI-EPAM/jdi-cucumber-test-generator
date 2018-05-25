package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

import com.epam.test_generator.config.security.AuthenticatedUser;
import com.epam.test_generator.controllers.project.ProjectTransformer;
import com.epam.test_generator.dao.impl.JiraFilterDAO;
import com.epam.test_generator.dao.impl.JiraProjectDAO;
import com.epam.test_generator.dao.impl.JiraStoryDAO;
import com.epam.test_generator.dao.impl.JiraSubStoryDAO;
import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.ProjectDAO;
import com.epam.test_generator.dao.interfaces.RemovedIssueDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dao.interfaces.SuitVersionDAO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.RemovedIssue;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.User;
import com.epam.test_generator.pojo.JiraFilter;
import com.epam.test_generator.pojo.JiraProject;
import com.epam.test_generator.pojo.JiraStatus;
import com.epam.test_generator.pojo.JiraStory;
import com.epam.test_generator.pojo.JiraSubTask;
import com.epam.test_generator.pojo.PropertyDifference;
import com.epam.test_generator.pojo.SuitVersion;
import com.epam.test_generator.services.exceptions.JiraRuntimeException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;
import net.rcarz.jiraclient.JiraException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JiraService {

    @Autowired
    private JiraSubStoryDAO jiraSubStoryDAO;

    @Autowired
    private JiraProjectDAO jiraProjectDAO;

    @Autowired
    private JiraStoryDAO jiraStoryDAO;

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private SuitDAO suitDAO;

    @Autowired
    private CaseDAO caseDAO;

    @Autowired
    private ProjectTransformer projectTransformer;

    @Autowired
    private RemovedIssueDAO removedIssueDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private JiraFilterDAO jiraFilterDAO;


    public List<JiraFilter> getAllFilters(Long clientId) {
        return jiraFilterDAO.getFilters(clientId);
    }

    @Autowired
    private SuitVersionDAO suitVersionDAO;


    /**
     * Creates project from Jira in the system with specified jira stories (suits in BDD) and all
     * subtasks (cases in BDD) of these stories.
     *
     * @param stories collection of Jira stories
     */
    public Project createProjectWithJiraStories(Long clientId, String jiraProjectKey,
                                                List<JiraStory> stories,
                                                Authentication auth) {
        if (!stories.isEmpty()) {
            if (areAllStoriesBelongsToProjectWithThisProjectKey(jiraProjectKey, stories)) {
                JiraProject projectByJiraKey = jiraProjectDAO
                    .getProjectByJiraKey(clientId, jiraProjectKey);
                return createProjectFromJiraProject(projectByJiraKey, auth, stories);
            } else {
                throw new JiraRuntimeException("There are some stories from another project");
            }
        }
        return null;
    }

    private Boolean areAllStoriesBelongsToProjectWithThisProjectKey(String jiraProjectKey,
                                                                    List<JiraStory> stories) {
        return stories
            .stream()
            .map(JiraStory::getJiraProjectKey)
            .allMatch(jiraProjectKey::equals);

    }

    public ProjectDTO createProjectWithAttachedFilters(Long clienId,
                                                       String jiraProjectKey,
                                                       List<JiraFilter> jiraFilters,
                                                       Authentication auth) {
        return projectTransformer
            .toDto(createProjectWithJiraStories(
                clienId,
                jiraProjectKey,
                findStoriesByFilters(clienId, jiraFilters),
                auth));
    }

    private List<JiraStory> findStoriesByFilters(Long clientId, List<JiraFilter> jiraFilters) {
        return jiraFilters
            .stream()
            .map(JiraFilter::getId)
            .map(fId -> jiraFilterDAO.getFilterByFilterId(clientId, fId))
            .flatMap(jql -> jiraStoryDAO.getJiraStoriesByFilter(clientId, jql).stream())
            .distinct()
            .collect(Collectors.toList());
    }

    /**
     * Creates specified jira stories (suits in BDD) and all subtasks (cases in BDD) of these
     * stories for already existed in BDD project.
     *
     * @param stories collection of Jira stories
     */
    public Project addStoriesToExistedProject(List<JiraStory> stories, String projectKey) {
        Project project = checkNotNull(projectDAO.findByJiraKey(projectKey));
        project.addSuits(mapJiraStoriesToSuits(stories));
        return projectDAO.save(project);
    }

    private List<Suit> mapJiraStoriesToSuits(List<JiraStory> stories) {
        return stories.stream()
            .map(this::createSuitFromJiraStory)
            .collect(Collectors.toList());
    }


    public List<JiraStory> getStories(Long clientId, String jiraProjectKey) throws JiraException {
        return jiraStoryDAO.getStories(clientId, jiraProjectKey);
    }


    /**
     * Returns stories which are not in the system from jira from specified project that is already
     * in the system.
     *
     * @param jiraKey clientId project in Jira
     * @return collection of Jira stories
     */
    public List<JiraStory> getJiraStoriesFromJiraProjectByProjectId(Long clientId, String jiraKey) {
        return jiraStoryDAO.getNonexistentStoriesByProject(clientId, jiraKey);
    }

    /**
     * Creates and associates a new project in BDD to Jira project.
     *
     * @param jiraProject - new project from Jira
     */
    private Project createProjectFromJiraProject(JiraProject jiraProject, Authentication auth,
                                                 List<JiraStory> stories) {
        User user = userService
            .getUserByEmail(((AuthenticatedUser) auth.getPrincipal()).getEmail());

        Project project = new Project();

        project.setName(jiraProject.getName());
        project.setDescription(jiraProject.getDescription());
        project.setJiraKey(jiraProject.getJiraKey());
        project.setActive(true);
        project.setUsers(Collections.singleton(user));
        project.addSuits(mapJiraStoriesToSuits(stories));

        return projectDAO.save(project);
    }


    /**
     * Creates and associates a new suit in BDD to Jira story.
     *
     * @param jiraStory - new story from Jira
     */
    private Suit createSuitFromJiraStory(JiraStory jiraStory) {
        Suit suit = new Suit();
        suit.setName(jiraStory.getName());
        suit.setDescription(jiraStory.getDescription());
        suit.setJiraKey(jiraStory.getJiraKey());
        suit.setJiraProjectKey(jiraStory.getJiraProjectKey());
        suit.setPriority(getPriority(jiraStory.getPriority()));
        suit.setLastModifiedDate(ZonedDateTime.now());
        suit.setLastJiraSyncDate(suit.getLastModifiedDate());
        suit.setStatus(getBDDStatus(jiraStory));
        return suit;
    }

    /**
     * Creates and associates a new case in BDD to Jira sub story.
     *
     * @param jiraSubTask - new sub story from Jira
     */
    private void createCaseFromJiraSubTask(JiraSubTask jiraSubTask) {
        Case caze = new Case();
        caze.setName(jiraSubTask.getName());
        caze.setDescription(jiraSubTask.getDescription());
        caze.setPriority(getPriority(jiraSubTask.getPriority()));
        caze.setJiraKey(jiraSubTask.getJiraKey());
        caze.setJiraParentKey(jiraSubTask.getJiraParentKey());
        caze.setJiraProjectKey(jiraSubTask.getJiraProjectKey());
        caze.setLastModifiedDate(ZonedDateTime.now());
        caze.setLastJiraSyncDate(caze.getLastModifiedDate());

        caseDAO.save(caze);

        Suit suit = suitDAO.findByJiraKey(jiraSubTask.getJiraParentKey());
        checkNotNull(suit);
        suit.getCases().add(caze);
    }


    /**
     * Updates existent suit in BDD by related Jira story.
     *
     * @param jiraStory - related issue from Jira
     */
    private void updateSuitFromJiraStory(JiraStory jiraStory) {
        Suit suit = suitDAO.findByJiraKey(jiraStory.getJiraKey());
        if (suit == null) {
            return;
        }
        suitDAO.save(suit);
    }

    private Status getBDDStatus(JiraStory jiraStory) {
        Status status;
        if (jiraStory.getStatus().equals(JiraStatus.RESOLVED)) {
            status = Status.PASSED;
        } else {
            status = Status.NOT_RUN;
        }
        return status;
    }


    /**
     * Updates existent case in BDD by related Jira sub story.
     *
     * @param jiraSubTask - related issue from Jira
     */
    private void updateSubTaskFromJiraSubTask(JiraSubTask jiraSubTask) {
        Case caze = caseDAO.findByJiraKey(jiraSubTask.getJiraKey());
        if (caze == null) {
            return;
        }
        caze.setName(jiraSubTask.getName());
        caze.setDescription(jiraSubTask.getDescription());
        caze.setPriority(getPriority(jiraSubTask.getPriority()));
        caze.setLastJiraSyncDate(ZonedDateTime.now());
        caseDAO.save(caze);
    }

    private Integer getPriority(String priority) {
        Integer intPriority;

        switch (priority) {
            case "Critical":
                intPriority = 1;
                break;
            case "Major":
                intPriority = 2;
                break;
            case "Minor":
                intPriority = 3;
                break;
            case "Trivial":
                intPriority = 4;
                break;
            case "Blocker":
                intPriority = 5;
                break;
            default:
                intPriority = null;
        }

        return intPriority;
    }


    /**
     * Returns all projects in Jira but non-existent in BBD
     *
     * @return list projects
     */
    public List<JiraProject> getNonexistentJiraProjects(Long clientId) {
        return jiraProjectDAO.getAllProjects(clientId).stream()
            .filter(project -> projectDAO.findByJiraKey(project.getJiraKey()) == null)
            .collect(Collectors.toList());
    }

    /**
     * Synchronisation method from Jira This method receives all related and opened stories and sub
     * stories from Jira and update its in BDD. <p> In case where issue has closed in Jira - the
     * changes not present in the response. By this reason all existing cases or suits which not
     * presents in response will be deleted from BBD.
     */

    private String generateQueryForSelectProjectsStories(String type) {
        String projects = projectDAO.findAll().stream()
            .filter(p -> p.getJiraKey() != null)
            .map(Project::getJiraKey)
            .collect(Collectors.joining(","));

        return String.format(
            "project in (%s) AND status in (Open, \"In Progress\", Reopened, Verified) AND type=%s",
            projects, type);

    }

    private void removeSuitsAndCasesFromBDD(Map<String, Suit> suits, Map<String, Case> cases) {
        cases.forEach((jiraKey, caze) -> {
            suitDAO.findByJiraKey(caze.getJiraParentKey()).getCases().remove(caze);
            caseDAO.delete(caze);
        });

        suits.forEach((jiraKey, suit) -> {
            projectDAO.findByJiraKey(suit.getJiraProjectKey()).getSuits().remove(suit);
            suitDAO.delete(suit);
        });
    }

    private void syncCaseByJiraSubtask(JiraSubTask subTask) {
        if (caseDAO.findByJiraKey(subTask.getJiraKey()) == null) {
            createCaseFromJiraSubTask(subTask);
        } else {
            updateSubTaskFromJiraSubTask(subTask);
        }
    }

    public void syncFromJira(Long clientId) {
        String storyQuery = generateQueryForSelectProjectsStories("story");
        String subtaskQuery = generateQueryForSelectProjectsStories("sub-task");

        List<JiraStory> jiraStories = jiraStoryDAO.getJiraStoriesByFilter(clientId, storyQuery);
        List<JiraSubTask> jiraSubTasks = jiraSubStoryDAO
            .getJiraSubtoriesByFilter(clientId, subtaskQuery);

        jiraStories.forEach(this::updateSuitFromJiraStory);

        jiraSubTasks.stream()
            .filter(subTask -> suitDAO.findByJiraKey(subTask.getJiraParentKey()) != null)
            .forEach(this::syncCaseByJiraSubtask);

        Map<String, Suit> suitsToBeDeleted = suitDAO.findAll().stream()
            .filter(suit ->
                jiraStories.stream()
                    .filter(jiraStory -> jiraStory.getJiraKey() != null)
                    .noneMatch(jiraStory -> jiraStory.getJiraKey().equals(suit.getJiraKey())))
            .collect(Collectors.toMap(Suit::getJiraKey, suit -> suit));

        Map<String, Case> casesToBeDeleted = caseDAO.findAll().stream()
            .filter(caze ->
                jiraSubTasks.stream()
                    .filter(jiraSubTask -> jiraSubTask.getJiraKey() != null)
                    .noneMatch(jiraSubTask -> jiraSubTask.getJiraKey().equals(caze.getJiraKey())))
            .collect(Collectors.toMap(Case::getJiraKey, caze -> caze));

        removeSuitsAndCasesFromBDD(suitsToBeDeleted, casesToBeDeleted);
    }


    private void closeRemovedSuitsInJira(Long clientId) {
        for (RemovedIssue issueToDeleteInJira : removedIssueDAO.findAll()) {
            jiraStoryDAO.changeStatusByJiraKey(clientId, issueToDeleteInJira.getJiraKey(),
                JiraStatus.CLOSED.getActionId());
            removedIssueDAO.delete(issueToDeleteInJira);
        }
    }

    private void createStoryWithSubTasksInJira(Long clientId, Suit suit) {

        jiraStoryDAO.createStory(clientId, suit);
        for (Case cases : suit.getCases()) {
            jiraSubStoryDAO.createSubStory(clientId, cases);
        }
    }

    private Status getStatusFromPropertyDiff(List<PropertyDifference> propertyDifferences) {
        for (PropertyDifference propertyDifference : propertyDifferences) {
            if (propertyDifference.getPropertyName().equals("status")) {
                return (Status) propertyDifference.getNewValue();
            }
        }
        return null;
    }

    private void updateStoryInJira(Long clientId, Suit suit) {
        Integer actionId;
        switch (suit.getStatus()) {
            case PASSED:
                actionId = JiraStatus.RESOLVED.getActionId();
                break;
            case FAILED:
                actionId = null;
                List<SuitVersion> suitVersions = suitVersionDAO.findAll(suit.getId());
                ListIterator<SuitVersion> iterator = suitVersions.listIterator(suitVersions.size());

                while (iterator.hasPrevious()) {
                    SuitVersion suitVersion = iterator.previous();
                    List<PropertyDifference> propertyDifferences = suitVersion
                        .getPropertyDifferences();
                    Status previousStatus = getStatusFromPropertyDiff(propertyDifferences);
                    if (!suit.getStatus().equals(previousStatus) && previousStatus != null &&
                        previousStatus.equals(Status.PASSED)) {
                        actionId = JiraStatus.REOPENED.getActionId();
                        break;
                    }
                }
                break;
            default:
                actionId = null;
        }

        if (actionId != null) {
            jiraStoryDAO.changeStatusByJiraKey(clientId, suit.getJiraKey(), actionId);
        }
    }

    private Boolean isSuitJustCreated(Suit suit) {
        return suit.getLastJiraSyncDate() == null;
    }

    private Boolean isSuitChangedAfterLastSync(Suit suit) {
        return suit.getLastModifiedDate().isAfter(suit.getLastJiraSyncDate());
    }

    private Boolean isCaseJustCreated(Case caze) {
        return caze.getLastJiraSyncDate() == null;
    }

    private Boolean isCaseChangedAfterLastSync(Case caze) {
        return caze.getLastModifiedDate().isAfter(caze.getLastJiraSyncDate());
    }

    /**
     * Synchronisation method to Jira The method puts all changes in BBD (all changed suits=stories,
     * cases=sub stories) to Jira in case when suit or case has deleted - stories/sub stories will
     * be closed in Jira too
     */
    public void syncToJira(Long clientId) {
        for (Suit suit : suitDAO.findAll()) {
            if (isSuitJustCreated(suit)) {
                createStoryWithSubTasksInJira(clientId, suit);
            } else if (isSuitChangedAfterLastSync(suit)) {
                updateStoryInJira(clientId, suit);
            }

            for (Case caze : suit.getCases()) {
                if (isCaseJustCreated(caze)) {
                    jiraSubStoryDAO.createSubStory(clientId, caze);
                } else if (isCaseChangedAfterLastSync(caze)) {
                    jiraSubStoryDAO.updateSubStoryByJiraKey(clientId, caze);
                }
            }
        }

        closeRemovedSuitsInJira(clientId);
    }
}
