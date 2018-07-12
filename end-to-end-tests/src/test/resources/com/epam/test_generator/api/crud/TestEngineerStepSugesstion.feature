Feature: CRUD StepSuggestion scenarios for TestEngineer


  Scenario Outline: Successfully create step suggestion
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    When I create StepSuggestion
    Then The StepSuggestion should be created

  Examples:
  | ROLE          |
  | TEST_ENGINEER |

  Scenario Outline: Successfully update step suggestion
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I create StepSuggestion
    When I update StepSuggestion
    Then The StepSuggestion should be updated

    Examples:
      | ROLE          |
      | TEST_ENGINEER |

  Scenario Outline: Successfully delete step suggestion
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I create StepSuggestion
    When I delete StepSuggestion
    Then The StepSuggestion should be deleted

    Examples:
      | ROLE          |
      | TEST_ENGINEER |

  Scenario Outline: Successfully search step suggestion
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
    When I search StepSuggestion
    Then I found Step Suggestion

    Examples:
      | ROLE          |
      | TEST_ENGINEER |