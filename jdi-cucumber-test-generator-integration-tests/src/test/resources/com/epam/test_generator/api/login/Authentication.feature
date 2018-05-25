Feature: Authentication

  Scenario: Sing in as user
    Given I have new user
    When I sign in
    Then I am signed in

    Scenario: I sing in
      When  I sign in with wrong data
        | email              | password |
        | testLead@email.com | qwerty1  |
      Then I get bad status

