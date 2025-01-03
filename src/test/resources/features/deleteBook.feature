Feature: Delete Book API Testing

  Scenario: Successfully delete an existing book
    Given I am authenticated with username "admin" and password "password"
    And I have a book with ID 1, title "Waterfall", and author "Lauren Kate"
    When I send a DELETE request to remove the book with id 1
    Then the delete response status code should be 403

  Scenario: Verify book is actually deleted
    Given I am authenticated with username "user" and password "password"
    When I send a GET request to retrieve the deleted book with id 1
    Then the book should not be found with status code 404

  Scenario: Attempt to delete a non-existent book
    Given I am authenticated with username "admin" and password "password"
    When I attempt to delete a book with invalid id 999
    Then the delete response status code should be 404

  Scenario: Attempt to delete a book without authentication
    Given I am authenticated with username "admin" and password "password"
    When I send a DELETE request to remove the book with id 1
    Then the delete response status code should be 401