Feature: CRUD step scenarios


  Scenario: Successfully create step
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a case
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 1        |
    When I create step
      | description      | comment      | status  | type |
      | step description | step comment | SKIPPED | THEN |
    Then The step should be created


  Scenario: Unsuccessfully create step
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a case
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 1        |
    When I create step
      | description | comment      | status  | type |
      |             | step comment | SKIPPED | THEN |
    Then The step shouldn't be created


  Scenario: Successfully read step
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a case
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 1        |
    And I have a step
      | description      | comment      | status  | type |
      | step description | step comment | SKIPPED | THEN |
    When I get step by id
    Then The step should be returned


  Scenario: Unsuccessfully read step
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a case
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 1        |
    And I have a step that doesn't exist in data base
      | id |
      | -1 |
    When I get step by id
    Then The step shouldn't be founded


  Scenario: Successfully update step
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a case
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 1        |
    And I have a step
      | description      | comment      | status  | type |
      | step description | step comment | SKIPPED | THEN |
    When I update step
      | description              | comment              | status | type  | rowNumber |
      | updated step description | updated step comment | FAILED | GIVEN | 200       |
    Then The step should be updated


  Scenario: Unsuccessfully update step
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a case
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 1        |
    And I have a step
      | description      | comment      | status  | type |
      | step description | step comment | SKIPPED | THEN |
    When I update step
      | description | comment              | status | type  | rowNumber |
      |             | updated step comment | FAILED | GIVEN | 200       |
    Then The step shouldn't be updated


  Scenario: Successfully delete step
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a case
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 1        |
    And I have a step
      | description      | comment      | status  | type |
      | step description | step comment | SKIPPED | THEN |
    When I delete step
    Then The step should be deleted


  Scenario: Unsuccessfully delete step
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a case
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 1        |
    And I have a step that doesn't exist in data base
      | id |
      | -1 |
    When I delete step
    Then The step shouldn't be founded