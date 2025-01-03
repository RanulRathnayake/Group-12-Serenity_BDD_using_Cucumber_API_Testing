Feature: Update Book API Testing

  Scenario: Attempt to update a book with missing required fields (title/author) - Update Book
    Given I am logged in with the username "admin" and password "password" - Update Book
    And I have created a book with ID 4, title "book2", and author "book2 auth" - Update Book
    When I update the book with ID 4 to title "" and author "" - Update Book
    Then the response status code should be 400 - Update Book
    And Invalid | Empty Input Parameters in the Request should be indicated - Update Book

  Scenario: Attempt to update a book without authorization - Update Book
    When I am logged in with the username "user" and password "password" - Update Book
    And I have created a book with ID 5, title "Child", and author "Fiona" - Update Book
    When I update the book with ID 5 to title "Care" and author "Bam" - Update Book
    Then the response status code should be 403 - Update Book

  Scenario: Attempt to update a book with an invalid book ID - Update Book
    Given I am logged in with the username "admin" and password "password" - Update Book
    And I have created a book with ID 6, title "Sara", and author "Lauren" - Update Book
    When I update the book with ID 999 to title "Invalid Book" and author "No Author" - Update Book
    Then the response status code should be 404 - Update Book

  Scenario: Successfully update a book - Update Book
    Given I am logged in with the username "admin" and password "password" - Update Book
    And I have created a book with ID 1, title "book1", and author "book1 auth" - Update Book
    When I update the book with ID 1 to title "updated book1" and author "updated book1 auth" - Update Book
    Then the response status code should be 200 - Update Book
    And the book should be updated with the new details - Update Book

  Scenario: Attempt to update a book with missing title field - Update Book
    Given I am logged in with the username "admin" and password "password" - Update Book
    And I have created a book with ID 2, title "book2", and author "book2 auth" - Update Book
    When I update the book with ID 2 to title "" and author "updated book2 auth" - Update Book
    Then the response status code should be 400 - Update Book
    And Invalid | Empty Input Parameters in the Request should be indicated - Update Book

  Scenario: Attempt to update a book with missing author field - Update Book
    Given I am logged in with the username "admin" and password "password" - Update Book
    And I have created a book with ID 3, title "book3", and author "book3 auth" - Update Book
    When I update the book with ID 3 to title "updated book3" and author "" - Update Book
    Then the response status code should be 400 - Update Book
    And Invalid | Empty Input Parameters in the Request should be indicated - Update Book



