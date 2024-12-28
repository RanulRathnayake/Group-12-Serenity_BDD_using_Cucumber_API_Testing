Feature: Get All Book API Testing

  Scenario: Successfully get all books with admin credentials
    Given I am logged in with the username "admin" and password "password"
    When I request to get all books
    Then the response status code should be 200
    And the response should contain a list of books

  Scenario: Successfully get all books with user credentials
    Given I am logged in with the username "user" and password "password"
    When I request to get all books
    Then the response status code should be 200
    And the response should contain a list of books

  Scenario: Attempt to get all books without logging in
    Given I am not logged in
    When I request to get all books
    Then the response status code should be 401
    And the response should indicate that authentication is required



