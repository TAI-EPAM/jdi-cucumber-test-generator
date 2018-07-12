Feature: CRUD StepSuggestion scenarios for TestEngineer


  Scenario Outline: Successfully update step
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a case
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 1        |
    And I have a step
      | description | comment      | status  | type |
      | stepuniqid  | step comment | SKIPPED | GIVEN |
    And I search StepSuggestion
    When I update StepSuggestion
    Then The step should be updated by StepSuggestion

    Examples:
      | ROLE          |
      | TEST_ENGINEER |

  Scenario Outline: Successfully delete step
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a case
      | name      | description      | comment      | priority |
      | case_name | case description | case comment | 1        |
    And I have a step
      | description | comment      | status  | type |
      | StepName  | step comment | SKIPPED | GIVEN |
    And I have a step
      | description | comment      | status  | type |
      | stepuniqid  | step comment | SKIPPED | GIVEN |
    And I search StepSuggestion
    When I delete StepSuggestion
    Then The step should be deleted

    Examples:
      | ROLE          |
      | TEST_ENGINEER |