Feature: case version scenarios

  Scenario: As a test engineer I want to get case history. Successful.
    Given I have user with role TEST_ENGINEER
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
      | description      | comment      | status  | type |
      | step description | step comment | SKIPPED | THEN |
    When  I get case version by case id
    Then The case version should be returned

  Scenario: As a test engineer I want to get case history. Unsuccessful.
    Given I have user with role TEST_ENGINEER
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a case that doesn't exist in data base
      | id |
      | -1 |
    When  I get case version by case id
    Then The case version shouldn't be returned