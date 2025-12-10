Feature: Log ind

  Background: Given the database is already populated with users

  Scenario: Log in as an active agent
    Given I have an active account
    And I am on the login page
    When i enter correct email and password
    And I click the login button
    Then I should be redirected to the dashboard
    And my role is available in the session/token