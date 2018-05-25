Feature: Test assigned projects

  Scenario: assigning TEST_LEAD to a project
    Given I have user with role TEST_LEAD
    And I have a project
      | name         | description         |
      | project_name | project_description |
    When I assign user to project
    Then The user is assigned to a project

  Scenario: assigning TEST_ENGINEER to a project
    Given I have user with role TEST_ENGINEER
    And I have a project
      | name         | description         |
      | project_name | project_description |
    When I assign user to project
    Then The user is assigned to a project

  Scenario: assigning GUEST to a project
    Given I have user with role GUEST
    And I have a project
      | name         | description         |
      | project_name | project_description |
    When I assign user to project
    Then The user is assigned to a project

  Scenario: Get list of TEST_LEAD projects
    Given I have user with role TEST_LEAD
    And I have a project
      | name         | description         |
      | project_name | project_description |
    When I assign user to project
    When I try to get projects
    Then I get projects

  Scenario: Get list of TEST_ENGINEER projects
    Given I have user with role TEST_ENGINEER
    And I have a project
      | name         | description         |
      | project_name | project_description |
    When I assign user to project
    When I try to get projects
    Then I get projects

