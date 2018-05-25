Feature: CRUD suit scenarios


  Scenario: Successfully create suit
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a list of tags
      | tag1 |
      | tag2 |
    When I create suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    Then The suit should be created


  Scenario: Unsuccessfully create suit
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a list of tags
      | tag1 |
      | tag2 |
    When I create suit
      | name      | description      | priority |
      | suit_name | suit_description | 0        |
    Then The suit shouldn't be created


  Scenario: Successfully read suit
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a list of tags
      | tag1 |
      | tag2 |
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    When I get suit by id
    Then The suit should be returned


  Scenario: Unsuccessfully read suit
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a list of tags
      | tag1 |
      | tag2 |
    And I have a suit that doesn't exist in data base
      | id |
      | -1 |
    When I get suit by id
    Then The suit shouldn't be founded


  Scenario: Successfully update suit
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a list of tags
      | tag1 |
      | tag2 |
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have a list of tags
      | tag2 |
      | tag3 |
    When I update suit
      | name         | description         | priority |
      | updated_name | updated_description | 2        |
    Then The suit should be updated


  Scenario: Unsuccessfully update suit
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a list of tags
      | tag1 |
      | tag2 |
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    When I update suit
      | name      | description      | priority |
      | suit_name | suit_description | 0        |
    Then The suit shouldn't be updated


  Scenario: Successfully delete suit
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a list of tags
      | tag1 |
      | tag2 |
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    When I delete suit
    Then The suit should be deleted


  Scenario: Unsuccessfully delete suit
    Given I login as user
      | email          | password |
      | admin@mail.com | admin    |
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I have a list of tags
      | tag1 |
      | tag2 |
    And I have a suit that doesn't exist in data base
      | id |
      | -1 |
    When I delete suit
    Then The suit shouldn't be founded