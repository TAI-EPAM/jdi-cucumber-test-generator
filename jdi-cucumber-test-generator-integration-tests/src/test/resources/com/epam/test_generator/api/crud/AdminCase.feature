Feature: CRUD case scenarios


  Scenario: Successfully create case
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
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


  Scenario: Unsuccessfully create case
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    When I create case
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 0        |
    Then The case shouldn't be created


  Scenario: Successfully read case
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a list of tags
      | tag1 |
      | tag2 |
    And I have a case
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 1        |
    When I get case by id
    Then The case should be returned


  Scenario: Unsuccessfully read case
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a case that doesn't exist in data base
      | id |
      | -1 |
    When I get case by id
    Then The case shouldn't be founded


  Scenario: Successfully update case
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
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


  Scenario: Unsuccessfully update case
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
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


  Scenario: Successfully delete case
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
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


  Scenario: Unsuccessfully delete case
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a case that doesn't exist in data base
      | id |
      | -1 |
    When I delete case
    Then The case shouldn't be founded