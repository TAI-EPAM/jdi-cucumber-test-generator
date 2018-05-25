Feature: Test engineer sets step status
    
Scenario Outline: One step
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
    And I create step
        | description      | comment      | status  | type |
        | step description | step comment | SKIPPED | THEN |

    When I set status '<status>' for step

    Then Step should have status '<status>'

Examples:
    | status  |
    | PASSED  |
    | FAILED  |
    | SKIPPED |
    | NOT RUN |