Feature: Create Book API Testing

  Scenario: Successfully create a book with valid data
    Given I am logged in with the username "admin" and password "password"
    When I create a new book with id 1 title "Book01" and author "Author01"
    Then the response status code should be 201
    And the book should be created with id 1 title "Book01" and author "Author01"

  Scenario: Attempt to create a book with missing title
    Given I am logged in with the username "admin" and password "password"
    When I attempt to create a book with id 2 and author "Author02" but missing title ""
    Then the response status code should be 201 for missing title
#    And the error message should indicate missing title

