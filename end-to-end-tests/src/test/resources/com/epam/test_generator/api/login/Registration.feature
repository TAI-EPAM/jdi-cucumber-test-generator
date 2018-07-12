Feature: Registration

  Scenario: registration
    When I have new user
    Then User is created

  Scenario: register a user with duplicate e-mail
    Given I register a new user
      | email              | name | surname | password |
      | testLead@email.com | user | test    | qwerty   |
    When I register a new user
      | email              | name | surname | password |
      | testLead@email.com | user | test    | qwerty   |
    Then I get bad status