Feature: CRUD project scenarios


  Scenario: Successfully create project
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    When I create project
      | name         | description         |
      | project name | project description |
    Then The project should be created


  Scenario: Unsuccessfully create project
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    When I create project
      | name | description         |
      |      | project description |
    Then The project shouldn't be created


  Scenario: Successfully read project
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project name | project description |
    When I get project by id
    Then The project should be returned


  Scenario: Unsuccessfully read project
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project that doesn't exist in data base
      | id |
      | -1 |
    When I get project by id
    Then The project shouldn't be founded


  Scenario: Successfully update project
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project name | project description |
    When I update project
      | name                 | description                 |
      | updated project name | updated project description |
    Then The project should be updated


  Scenario: Unsuccessfully update project
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project name | project description |
    When I update project
      | name | description         |
      |      | project description |
    Then The project shouldn't be updated


  Scenario: Successfully close project
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project name | project description |
    When I close project
    Then The project should be closed


  Scenario: Unsuccessfully close project
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project that doesn't exist in data base
      | id |
      | -1 |
    When I close project
    Then The project shouldn't be founded