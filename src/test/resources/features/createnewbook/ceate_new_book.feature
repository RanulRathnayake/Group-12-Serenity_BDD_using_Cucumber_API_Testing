Feature: Create a new book

  Scenario: Successfully create a new book
    Given I have a valid book payload
    When I send a POST request to "/api/books"
    Then the response status should be 200
    And the response should contain the book details
