package com.epam.test_generator.api;

import static io.restassured.http.ContentType.JSON;

import com.epam.http.annotations.ContentType;
import com.epam.http.annotations.DELETE;
import com.epam.http.annotations.GET;
import com.epam.http.annotations.POST;
import com.epam.http.annotations.PUT;
import com.epam.http.annotations.ServiceDomain;
import com.epam.http.requests.RestMethod;

/**
 * Represents the EPAM JDI Http Client for BBD Generator REST API.
 * Automatically generated from BDD Generator's Swagger OpenAPI Specification.
 */
@ServiceDomain("http://localhost:8080/")
public class BddGeneratorApi {
	@ContentType(JSON)
	@GET("/admin/jira-settings")
	public static RestMethod getJiraSettingsUsingGET;

	@ContentType(JSON)
	@POST("/admin/jira-settings")
	public static RestMethod createJiraSettingsUsingPOST;

	@ContentType(JSON)
	@GET("/admin/projects")
	public static RestMethod getProjects;

	@ContentType(JSON)
	@DELETE("/admin/projects/{projectId}")
	public static RestMethod removeProject;

	@ContentType(JSON)
	@PUT("/admin/role")
	public static RestMethod changeUserRoleUsingPUT;

	@ContentType(JSON)
	@GET("/admin/users")
	public static RestMethod getUsersUsingGET;

	@ContentType(JSON)
	@GET("/events")
	public static RestMethod getEvents;

	@ContentType(JSON)
	@GET("/events/statuses")
	public static RestMethod getEvents_1;

	@ContentType(JSON)
	@GET("/events/{status}")
	public static RestMethod getAvailableEvents;

	@ContentType(JSON)
	@POST("/jenkins/job/execute")
	public static RestMethod executeJobUsingPOST;

	@ContentType(JSON)
	@GET("/jenkins/jobs")
	public static RestMethod getJobsUsingGET;

	@ContentType(JSON)
	@PUT("/jira/jira-settings/{jiraSettingsId}/export")
	public static RestMethod syncToJiraUsingPUT;

	@ContentType(JSON)
	@PUT("/jira/jira-settings/{jiraSettingsId}/import")
	public static RestMethod syncFromJiraUsingPUT;

	@ContentType(JSON)
	@GET("/jira/jira-settings/{jiraSettingsId}/jira-filters")
	public static RestMethod getFiltersUsingGET;

	@ContentType(JSON)
	@POST("/jira/jira-settings/{jiraSettingsId}/project-by-filters/{jiraKey}")
	public static RestMethod createProjectByFiltersUsingPOST;

	@ContentType(JSON)
	@POST("/jira/jira-settings/{jiraSettingsId}/project/{jiraKey}")
	public static RestMethod createProjectWithAttFromJiraUsingPOST;

	@ContentType(JSON)
	@GET("/jira/jira-settings/{jiraSettingsId}/project/{jiraKey}/stories")
	public static RestMethod getAllStoriesUsingGET;

	@ContentType(JSON)
	@GET("/jira/jira-settings/{jiraSettingsId}/projects")
	public static RestMethod getProjectsUsingGET;

	@ContentType(JSON)
	@POST("/jira/project/{jiraKey}/suits")
	public static RestMethod createStoriesForProjectUsingPOST;

	@ContentType(JSON)
	@POST("/login")
	public static RestMethod loginUsingPOST;

	@ContentType(JSON)
	@GET("/proba/opa/{projectId}")
	public static RestMethod getTestRunResultsFromToUsingGET;

	@ContentType(JSON)
	@GET("/projects")
	public static RestMethod getUserProjects;

	@ContentType(JSON)
	@POST("/projects")
	public static RestMethod createProject;

	@ContentType(JSON)
	@GET("/projects/{projectId}")
	public static RestMethod getProject;

	@ContentType(JSON)
	@PUT("/projects/{projectId}")
	public static RestMethod updateProject;

	@ContentType(JSON)
	@DELETE("/projects/{projectId}")
	public static RestMethod closeProject;

	@ContentType(JSON)
	@GET("/projects/{projectId}/suits")
	public static RestMethod getSuits;

	@ContentType(JSON)
	@POST("/projects/{projectId}/suits")
	public static RestMethod createSuit;

	@ContentType(JSON)
	@PUT("/projects/{projectId}/suits/row-numbers")
	public static RestMethod updateSuitRowNumber;

	@ContentType(JSON)
	@GET("/projects/{projectId}/suits/{suitId}")
	public static RestMethod getProjectSuit;

	@ContentType(JSON)
	@PUT("/projects/{projectId}/suits/{suitId}")
	public static RestMethod updateSuit;

	@ContentType(JSON)
	@DELETE("/projects/{projectId}/suits/{suitId}")
	public static RestMethod removeSuit;

	@ContentType(JSON)
	@GET("/projects/{projectId}/suits/{suitId}/cases")
	public static RestMethod getCases;

	@ContentType(JSON)
	@PUT("/projects/{projectId}/suits/{suitId}/cases")
	public static RestMethod updateCases;

	@ContentType(JSON)
	@POST("/projects/{projectId}/suits/{suitId}/cases")
	public static RestMethod addCaseToSuit;

	@ContentType(JSON)
	@DELETE("/projects/{projectId}/suits/{suitId}/cases")
	public static RestMethod removeCases;

	@ContentType(JSON)
	@PUT("/projects/{projectId}/suits/{suitId}/cases/row-numbers")
	public static RestMethod updateCaseRowNumber;

	@ContentType(JSON)
	@GET("/projects/{projectId}/suits/{suitId}/cases/{caseId}")
	public static RestMethod getCase;

	@ContentType(JSON)
	@PUT("/projects/{projectId}/suits/{suitId}/cases/{caseId}")
	public static RestMethod updateCase;

	@ContentType(JSON)
	@DELETE("/projects/{projectId}/suits/{suitId}/cases/{caseId}")
	public static RestMethod removeCase;

	@ContentType(JSON)
	@PUT("/projects/{projectId}/suits/{suitId}/cases/{caseId}/events/{event}")
	public static RestMethod performEvent;

	@ContentType(JSON)
	@GET("/projects/{projectId}/suits/{suitId}/cases/{caseId}/steps")
	public static RestMethod getSteps;

	@ContentType(JSON)
	@POST("/projects/{projectId}/suits/{suitId}/cases/{caseId}/steps")
	public static RestMethod addStepToCase;

	@ContentType(JSON)
	@GET("/projects/{projectId}/suits/{suitId}/cases/{caseId}/steps/{stepId}")
	public static RestMethod getStep;

	@ContentType(JSON)
	@PUT("/projects/{projectId}/suits/{suitId}/cases/{caseId}/steps/{stepId}")
	public static RestMethod updateStep;

	@ContentType(JSON)
	@DELETE("/projects/{projectId}/suits/{suitId}/cases/{caseId}/steps/{stepId}")
	public static RestMethod removeCase_1;

	@ContentType(JSON)
	@GET("/projects/{projectId}/suits/{suitId}/cases/{caseId}/versions")
	public static RestMethod getCaseVersions;

	@ContentType(JSON)
	@PUT("/projects/{projectId}/suits/{suitId}/cases/{caseId}/versions/{commitId}")
	public static RestMethod restoreCase;

	@ContentType(JSON)
	@POST("/projects/{projectId}/suits/{suitId}/feature-file")
	public static RestMethod downloadFileUsingPOST;

	@ContentType(JSON)
	@GET("/projects/{projectId}/suits/{suitId}/versions")
	public static RestMethod getSuitVersions;

	@ContentType(JSON)
	@PUT("/projects/{projectId}/suits/{suitId}/versions/{commitId}")
	public static RestMethod restoreSuit;

	@ContentType(JSON)
	@POST("/projects/{projectId}/tests")
	public static RestMethod runTests;

	@ContentType(JSON)
	@GET("/projects/{projectId}/tests/results")
	public static RestMethod getTestRunResultsFromTo;

	@ContentType(JSON)
	@GET("/projects/{projectId}/tests/results/dates")
	public static RestMethod getTestRunResultsFromDatesRange;

	@ContentType(JSON)
	@PUT("/projects/{projectId}/user/{userId}")
	public static RestMethod addUserToProject;

	@ContentType(JSON)
	@DELETE("/projects/{projectId}/user/{userId}")
	public static RestMethod removeUserFromProject;

	@ContentType(JSON)
	@GET("/refresh-token")
	public static RestMethod refreshTokenUsingGET;

	@ContentType(JSON)
	@GET("/step-suggestions")
	public static RestMethod getStepsSuggestions;

	@ContentType(JSON)
	@POST("/step-suggestions")
	public static RestMethod addStepSuggestion;

	@ContentType(JSON)
	@GET("/step-suggestions/search")
	public static RestMethod findStepsSuggestions;

	@ContentType(JSON)
	@PUT("/step-suggestions/{stepSuggestionId}")
	public static RestMethod updateStepSuggestion;

	@ContentType(JSON)
	@DELETE("/step-suggestions/{stepSuggestionId}")
	public static RestMethod removeStepSuggestion;

	@ContentType(JSON)
	@GET("/step-suggestions/{stepType}")
	public static RestMethod getStepsSuggestionsByType;

	@ContentType(JSON)
	@POST("/user/change-password")
	public static RestMethod passwordResetUsingPOST;

	@ContentType(JSON)
	@GET("/user/confirm-email")
	public static RestMethod confirmEmailUsingGET;

	@ContentType(JSON)
	@POST("/user/forgot-password")
	public static RestMethod passwordForgotUsingPOST;

	@ContentType(JSON)
	@POST("/user/registration")
	public static RestMethod registerUserAccountUsingPOST;

	@ContentType(JSON)
	@GET("/user/validate-reset-token")
	public static RestMethod displayResetPasswordPageUsingGET;
}
