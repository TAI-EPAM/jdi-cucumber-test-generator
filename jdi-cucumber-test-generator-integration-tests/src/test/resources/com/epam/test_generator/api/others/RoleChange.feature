Feature: Role change

  Scenario: change user role on TEST_ENGINEER
    Given I logged in as admin
    And I have new user
    When I change user role on TEST_ENGINEER
    Then User role must be changed

  Scenario: change user role on TEST_LEAD
    Given I logged in as admin
    And I have new user
    When I change user role on TEST_LEAD
    Then User role must be changed

  Scenario: change user role on GUEST
    Given I logged in as admin
    And I have new user
    When I change user role on GUEST
    Then User role must be changed

#  Scenario: change user role on ADMIN
#    Given I logged in as admin
#    And I have new user
#    When I change user role on ADMIN
#    Then User role should not be changed
