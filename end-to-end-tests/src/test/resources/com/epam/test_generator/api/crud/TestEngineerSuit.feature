Feature: CRUD Suit scenarios for TestEngineer and TestLead

  Scenario Outline: Successfully create suit
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I have a list of tags
      | tag1 |
      | tag2 |
    When I create suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    Then The suit should be created

  Examples:
  | ROLE   |
  | TEST_ENGINEER |
  | TEST_LEAD |

  Scenario Outline: Unsuccessfully create suit
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I have a list of tags
      | tag1 |
      | tag2 |
    When I create suit
      | name      | description      | priority |
      | suit_name | suit_description | 0        |
    Then The suit shouldn't be created

  Examples:
  | ROLE   |
  | TEST_ENGINEER |
  | TEST_LEAD |

  Scenario Outline: Successfully read suit
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I have a list of tags
      | tag1 |
      | tag2 |
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    When I get suit by id
    Then The suit should be returned

  Examples:
  | ROLE   |
  | TEST_ENGINEER |
  | TEST_LEAD |

  Scenario Outline: Unsuccessfully read suit
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I have a list of tags
      | tag1 |
      | tag2 |
    And I have a suit that doesn't exist in data base
      | id |
      | -1 |
    When I get suit by id
    Then The suit shouldn't be found

  Examples:
  | ROLE   |
  | TEST_ENGINEER |
  | TEST_LEAD |

  Scenario Outline: Successfully update suit
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
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

  Examples:
  | ROLE   |
  | TEST_ENGINEER |
  | TEST_LEAD |

  Scenario Outline: Unsuccessfully update suit
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
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

  Examples:
  | ROLE   |
  | TEST_ENGINEER |
  | TEST_LEAD |

  Scenario Outline: Successfully delete suit
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I have a list of tags
      | tag1 |
      | tag2 |
    And I have a suit
      | name      | description      | priority |
      | suit_name | suit_description | 1        |
    When I delete suit
    Then The suit should be deleted

  Examples:
  | ROLE   |
  | TEST_ENGINEER |
  | TEST_LEAD |

  Scenario Outline: Unsuccessfully delete suit
    Given I have user with role '<ROLE>'
    And I have a project
      | name         | description         |
      | project_name | project_description |
    And I assign user to project
    And I have a list of tags
      | tag1 |
      | tag2 |
    And I have a suit that doesn't exist in data base
      | id |
      | -1 |
    When I delete suit
    Then The suit shouldn't be found

  Examples:
  | ROLE   |
  | TEST_ENGINEER |
  | TEST_LEAD |