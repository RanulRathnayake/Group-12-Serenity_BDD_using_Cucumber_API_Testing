Feature: Get Book API Testing

  Scenario: Successfully create a book with valid data
    Given I am logged in with the username "admin" and password "password"
    When I create a new book with id 1 title "Book01" and author "Author01"
    Then the response status code should be 200
    And the book should be created with id 1 title "Book01" and author "Author01"

  Scenario: Successfully get a specific book with admin credentials
    Given I am logged in with the username "admin" and password "password"
    And the book ID is "1"
    When I request to get the book
    Then the response status code should be 200
    And the response should contain the book details

  Scenario: Successfully get a specific book with user credentials
    Given I am logged in with the username "user" and password "password"
    And the book ID is "1"
    When I request to get the book
    Then the response status code should be 200
    And the response should contain the book details

  Scenario: Attempt to get a specific book without logging in
    Given I am not logged in
    And the book ID is "1"
    When I request to get the book
    Then the response status code should be 401
    And the response should indicate that authentication is required
