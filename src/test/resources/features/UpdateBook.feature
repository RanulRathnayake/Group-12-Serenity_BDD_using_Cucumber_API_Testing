Feature: Update Book API Testing

  Scenario: Successfully update a book
    Given I am logged in with the username "admin" and password "password"
    And I have a book with ID "1", title "Waterfall", and author "Lauren Kate"
    When I update the book with ID "1" to title "Harry Potter" and author "JK Rowling"
    Then the response status code should be 200
    And the book should be updated with the new details

  Scenario: Attempt to update a book with missing required fields (title/author)
    Given I am logged in with the username "admin" and password "password"
    And I have a book with ID "1", title "Waterfall", and author "Lauren Kate"
    When I update the book with ID "1" to title "" and author ""
    Then the response status code should be 400
    And Invalid | Empty Input Parameters in the Request should be indicated
