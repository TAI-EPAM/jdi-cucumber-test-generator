package com.epam.test_generator.api;

import static io.restassured.http.ContentType.JSON;

import com.epam.http.annotations.ContentType;
import com.epam.http.annotations.DELETE;
import com.epam.http.annotations.GET;
import com.epam.http.annotations.Header;
import com.epam.http.annotations.POST;
import com.epam.http.annotations.PUT;
import com.epam.http.annotations.ServiceDomain;
import com.epam.http.requests.RestMethod;
import java.lang.String;

/**
 * Represents the EPAM JDI Http Client for BBD Generator REST API.
 * Automatically generated from BDD Generator's Swagger OpenAPI Specification.
 */
@ServiceDomain("http://localhost:8080/cucumber")
public class BddGeneratorApi {
	private static final String AUTHORIZATION_PARAMETER_NAME = "Authorization";

	private static final String AUTHORIZATION_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJpc3MiOiJjdWN1bWJlciIsImlkIjo1LCJnaXZlbl9uYW1lIjoiYWRtaW5OYW1lIiwiZmFtaWx5X25hbWUiOiJhZG1pblN1cm5hbWUiLCJlbWFpbCI6ImFkbWluQG1haWwuY29tIn0.0pF_BkGW_1NdihCY4J-7wk7iRkLJs9vMzF-T8zHXFcA";

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@PUT("/admin/changeroles")
	static RestMethod changeUserRoleUsingPUT;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/admin/projects")
	static RestMethod getProjects;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@DELETE("/admin/projects/{projectId}")
	static RestMethod removeProject;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/admin/users")
	static RestMethod getUsersUsingGET;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/confirmAccount")
	static RestMethod displayResetPasswordPageUsingGET_1;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/events")
	static RestMethod getEvents;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/events/{status}")
	static RestMethod getAvailableEvents;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/jenkins/job")
	static RestMethod getJobsUsingGET;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@POST("/jenkins/job/execute")
	static RestMethod executeJobUsingPOST;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@POST("/login")
	static RestMethod loginUsingPOST;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@POST("/passwordForgot")
	static RestMethod passwordForgotUsingPOST;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/passwordReset")
	static RestMethod displayResetPasswordPageUsingGET;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@POST("/passwordReset")
	static RestMethod passwordResetUsingPOST;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/projects")
	static RestMethod getUserProjects;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@POST("/projects")
	static RestMethod createProject;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/projects/{projectId}")
	static RestMethod getProject;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@PUT("/projects/{projectId}")
	static RestMethod updateProject;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@DELETE("/projects/{projectId}")
	static RestMethod closeProject;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/projects/{projectId}/suits")
	static RestMethod getSuits;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@POST("/projects/{projectId}/suits")
	static RestMethod createSuit;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@PUT("/projects/{projectId}/suits/updateRowNumbers")
	static RestMethod updateSuitRowNumber;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/projects/{projectId}/suits/{suitId}")
	static RestMethod getProjectSuit;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@PUT("/projects/{projectId}/suits/{suitId}")
	static RestMethod updateSuit;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@DELETE("/projects/{projectId}/suits/{suitId}")
	static RestMethod removeSuit;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/projects/{projectId}/suits/{suitId}/cases")
	static RestMethod getCases;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@PUT("/projects/{projectId}/suits/{suitId}/cases")
	static RestMethod updateCases;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@POST("/projects/{projectId}/suits/{suitId}/cases")
	static RestMethod addCaseToSuit;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@DELETE("/projects/{projectId}/suits/{suitId}/cases")
	static RestMethod removeCases;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/projects/{projectId}/suits/{suitId}/cases/tags")
	static RestMethod getAllTagsFromAllCasesInSuit;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/projects/{projectId}/suits/{suitId}/cases/{caseId}")
	static RestMethod getCase;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@PUT("/projects/{projectId}/suits/{suitId}/cases/{caseId}")
	static RestMethod updateCase;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@DELETE("/projects/{projectId}/suits/{suitId}/cases/{caseId}")
	static RestMethod removeCase;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@PUT("/projects/{projectId}/suits/{suitId}/cases/{caseId}/events/{event}")
	static RestMethod performEvent;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/projects/{projectId}/suits/{suitId}/cases/{caseId}/steps")
	static RestMethod getSteps;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@PUT("/projects/{projectId}/suits/{suitId}/cases/{caseId}/steps")
	static RestMethod updateSteps;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@POST("/projects/{projectId}/suits/{suitId}/cases/{caseId}/steps")
	static RestMethod addStepToCase;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/projects/{projectId}/suits/{suitId}/cases/{caseId}/steps/{stepId}")
	static RestMethod getStep;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@PUT("/projects/{projectId}/suits/{suitId}/cases/{caseId}/steps/{stepId}")
	static RestMethod updateStep;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@DELETE("/projects/{projectId}/suits/{suitId}/cases/{caseId}/steps/{stepId}")
	static RestMethod removeCase_1;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/projects/{projectId}/suits/{suitId}/cases/{caseId}/tags")
	static RestMethod getTags;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@POST("/projects/{projectId}/suits/{suitId}/cases/{caseId}/tags")
	static RestMethod addTagToCase;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@PUT("/projects/{projectId}/suits/{suitId}/cases/{caseId}/tags/{tagId}")
	static RestMethod updateTag;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@DELETE("/projects/{projectId}/suits/{suitId}/cases/{caseId}/tags/{tagId}")
	static RestMethod removeTag;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/projects/{projectId}/suits/{suitId}/cases/{caseId}/versions")
	static RestMethod getCaseVersions;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@PUT("/projects/{projectId}/suits/{suitId}/cases/{caseId}/versions/{commitId}")
	static RestMethod restoreCase;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@POST("/projects/{projectId}/suits/{suitId}/featureFile")
	static RestMethod downloadFileUsingPOST;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@PUT("/projects/{projectId}/users")
	static RestMethod addUserToProject;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@DELETE("/projects/{projectId}/users")
	static RestMethod removeUserFromProject;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@POST("/registration")
	static RestMethod registerUserAccountUsingPOST;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/statuses")
	static RestMethod getEvents_1;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/stepSuggestions")
	static RestMethod getStepsSuggestions;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@POST("/stepSuggestions")
	static RestMethod addStepSuggestion;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@PUT("/stepSuggestions/{stepSuggestionId}")
	static RestMethod updateStepSuggestion;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@DELETE("/stepSuggestions/{stepSuggestionId}")
	static RestMethod removeStepSuggestion;

	@Header(
			name = AUTHORIZATION_PARAMETER_NAME,
			value = AUTHORIZATION_TOKEN
	)
	@ContentType(JSON)
	@GET("/stepSuggestions/{stepType}")
	static RestMethod getStepsSuggestionsByType;
}
