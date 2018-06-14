Feature: CRUD Step scenarios for TestEngineer and TestLead


  Scenario Outline: Successfully create step
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
    When I create step
      | description      | comment      | status  | type |
      | step description | step comment | SKIPPED | THEN |
    Then The step should be created

  Examples:
  | ROLE   |
  | TEST_ENGINEER |
  | TEST_LEAD |

  Scenario Outline: Unsuccessfully create step
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
    When I create step
      | description | comment      | status  | type |
      |             | step comment | SKIPPED | THEN |
    Then The step shouldn't be created

  Examples:
  | ROLE   |
  | TEST_ENGINEER |
  | TEST_LEAD |

  Scenario Outline: Successfully read step
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
      | description      | comment      | status  | type |
      | step description | step comment | SKIPPED | THEN |
    When I get step by id
    Then The step should be returned

  Examples:
  | ROLE   |
  | TEST_ENGINEER |
  | TEST_LEAD |

  Scenario Outline: Unsuccessfully read step
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
    And I have a step that doesn't exist in data base
      | id |
      | -1 |
    When I get step by id
    Then The step shouldn't be found

  Examples:
  | ROLE   |
  | TEST_ENGINEER |
  | TEST_LEAD |

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
      | description      | comment      | status  | type |
      | step description | step comment | SKIPPED | THEN |
    When I update step
      | description              | comment              | status | type  | rowNumber |
      | updated step description | updated step comment | FAILED | GIVEN | 200       |
    Then The step should be updated

  Examples:
  | ROLE   |
  | TEST_ENGINEER |
  | TEST_LEAD |

  Scenario Outline: Unsuccessfully update step
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
      | description      | comment      | status  | type |
      | step description | step comment | SKIPPED | THEN |
    When I update step
      | description | comment              | status | type  | rowNumber |
      |             | updated step comment | FAILED | GIVEN | 200       |
    Then The step shouldn't be updated

  Examples:
  | ROLE   |
  | TEST_ENGINEER |
  | TEST_LEAD |

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
      | case name | case description | case comment | 1        |
    And I have a step
      | description      | comment      | status  | type |
      | step description | step comment | SKIPPED | THEN |
    When I delete step
    Then The step should be deleted

  Examples:
  | ROLE   |
  | TEST_ENGINEER |
  | TEST_LEAD |

  Scenario Outline: Unsuccessfully delete step
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
    And I have a step that doesn't exist in data base
      | id |
      | -1 |
    When I delete step
    Then The step shouldn't be found

  Examples:
  | ROLE   |
  | TEST_ENGINEER |
  | TEST_LEAD |