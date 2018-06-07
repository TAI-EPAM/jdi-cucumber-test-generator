Feature: CRUD Case scenarios for TestEngineer and TestLead


  Scenario Outline: Successfully create case
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a list of tags
      | tag1 |
      | tag2 |
    When I create case
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 1        |
    Then The case should be created

  Examples:
  | ROLE  |
  | TEST_ENGINEER  |
  | TEST_LEAD  |


  Scenario Outline: Unsuccessfully create case
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    When I create case
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 0        |
    Then The case shouldn't be created

  Examples:
  | ROLE  |
  | TEST_ENGINEER  |
  | TEST_LEAD  |


  Scenario Outline: Successfully read case
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I assign user to project
    And I have a list of tags
      | tag1 |
      | tag2 |
    And I have a case
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 1        |
    When I get case by id
    Then The case should be returned

  Examples:
  | ROLE  |
  | TEST_ENGINEER  |
  | TEST_LEAD  |


  Scenario Outline: Unsuccessfully read case
    Given I have user with role '<ROLE>'
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
    When I get case by id
    Then The case shouldn't be founded

  Examples:
  | ROLE  |
  | TEST_ENGINEER  |
  | TEST_LEAD  |


  Scenario Outline: Successfully update case
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a list of tags
      | tag1 |
      | tag2 |
    And I have a case
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 1        |
    And I have a list of tags
      | tag2 |
      | tag3 |
    When I update case
      | name      | description      | comment      | priority | status |
      | case name | case description | case comment | 1        | FAILED |
    Then The case should be updated

  Examples:
  | ROLE  |
  | TEST_ENGINEER  |
  | TEST_LEAD  |


  Scenario Outline: Unsuccessfully update case
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a list of tags
      | tag1 |
      | tag2 |
    And I have a case
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 1        |
    When I update case
      | name      | description      | comment      | priority | status |
      | case name | case description | case comment | 0        | FAILED |
    Then The case shouldn't be updated

  Examples:
  | ROLE  |
  | TEST_ENGINEER  |
  | TEST_LEAD  |


  Scenario Outline: Successfully delete case
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a list of tags
      | tag1 |
      | tag2 |
    And I have a case
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 1        |
    When I delete case
    Then The case should be deleted
    Examples:
      | ROLE |
      | TEST_ENGINEER |
      | TEST_LEAD |


  Scenario Outline: Unsuccessfully delete case
    Given I have user with role '<ROLE>'
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
    When I delete case
    Then The case shouldn't be founded

    Examples:
      | ROLE   |
      | TEST_ENGINEER |
      | TEST_LEAD |