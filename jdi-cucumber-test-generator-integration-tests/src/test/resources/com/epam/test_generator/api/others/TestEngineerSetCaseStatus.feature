Feature: As a test engineer I want to set status for a case

  Scenario: In suit with 3 cases with status PASSED change one case's status to FAILED
    Given I have user with role TEST_ENGINEER
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have some cases
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 1        |
      | case name | case description | case comment | 1        |
      | case name | case description | case comment | 1        |


    When I set status 'PASSED' for all cases
    And I set status 'FAILED' for 1 cases

    Then Suit should have status 'FAILED'

  Scenario: In suit with 3 cases with status FAILED change one case's status to PASSED
    Given I have user with role TEST_ENGINEER
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have some cases
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 1        |
      | case name | case description | case comment | 1        |
      | case name | case description | case comment | 1        |

    When I set status 'FAILED' for all cases
    And I set status 'PASSED' for 1 cases

    Then Suit should have status 'FAILED'

  Scenario: Set all case's statuses to PASSED in suit
    Given I have user with role TEST_ENGINEER
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    And I have some cases
      | name      | description      | comment      | priority |
      | case name | case description | case comment | 1        |
      | case name | case description | case comment | 1        |
      | case name | case description | case comment | 1        |

    When I set status 'PASSED' for all cases

    Then Suit should have status 'PASSED'

  Scenario Outline: Change case's status, that is the only one in suit
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


    When I set status '<status>' for case

    Then Case should have status '<status>'
    And Suit should have status '<status>'

  Examples:
  | status |
  | PASSED |
  | FAILED |