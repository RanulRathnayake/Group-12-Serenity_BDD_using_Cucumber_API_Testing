Feature: Create Book API Testing

  Scenario: Successfully create a book with valid data as admin
    Given I am logged in with the username "admin" and password "password"
    When I create a new book with id 1 title "Book01" and author "Author01"
    Then the response status code should 201
    And the book should be created with id 1 title "Book01" and author "Author01"

  Scenario: Attempt to create a book with missing title
    Given I am logged in with the username "admin" and password "password"
    When I attempt to create a book with id 2 and author "Author02" but missing title ""
    Then the response status code should 400
    And the error message should contain "Title is required"

  Scenario: Attempt to create a book with duplicate valid data
    Given I am logged in with the username "admin" and password "password"
    When I create a new book with id 3 title "Book03" and author "Author03"
    And a book exists with id 3 title "Book03" and author "Author03"
    When I create a new book with id 3 title "Book03" and author "Author05"
    Then the response status code should 208
    And the error message should contain "Book Already Exists"


