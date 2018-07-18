Feature: As Admin I want to block and unblock user

  Scenario: Block a user
    Given I logged in as admin
    And I have new user
    When I block user
    Then User must be blocked

  Scenario: Unblock a user
    Given I logged in as admin
    And I have new user
    And I block user
    When I unblock user
    Then User must be unblocked

  Scenario: Can't find a user
    Given I logged in as admin
    When I block user with wrong id
    Then The user shouldn't be found