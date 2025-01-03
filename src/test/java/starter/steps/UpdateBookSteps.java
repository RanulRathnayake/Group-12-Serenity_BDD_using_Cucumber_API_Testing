package starter.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UpdateBookSteps {

    private Response response;
    private String baseUrl = "http://localhost:7081";
    private String expectedTitle;
    private String expectedAuthor;
    private String username;
    private String password;

    // Login
    @Given("I am logged in with the username {string} and password {string} - Update Book")
    public void i_am_logged_in_with_the_username_and_password(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Create a book
    @Given("I have created a book with ID {int}, title {string}, and author {string} - Update Book")
    public void i_have_created_a_book_with_id_title_and_author(Integer bookId, String title, String author) {
        String jsonBody = String.format("{\"id\": %d, \"title\": \"%s\", \"author\": \"%s\"}", bookId, title, author);

        given()
                .auth().basic(username, password)
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post(baseUrl + "/api/books")
                .then()
                .statusCode(201); // Ensure the book is created successfully
    }

    // Update book details
    @When("I update the book with ID {int} to title {string} and author {string} - Update Book")
    public void i_update_the_book_with_id_to_title_and_author(Integer bookId, String title, String author) {
        expectedTitle = title;
        expectedAuthor = author;

        String jsonBody = String.format("{\"id\": %d, \"title\": \"%s\", \"author\": \"%s\"}", bookId, title, author);

        response = given()
                .auth().basic(username, password)
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .put(baseUrl + "/api/books/" + bookId);
    }

    // Validate response status code
    @Then("the response status code should be {int} - Update Book")
    public void the_response_status_code_should_be(Integer expectedStatusCode) {
        assertThat(response.statusCode(), equalTo(expectedStatusCode));
    }

    // Validate updated book details
    @Then("the book should be updated with the new details - Update Book")
    public void the_book_should_be_updated_with_the_new_details() {
        String updatedTitle = response.jsonPath().getString("title");
        String updatedAuthor = response.jsonPath().getString("author");

        assertThat(updatedTitle, equalTo(expectedTitle));
        assertThat(updatedAuthor, equalTo(expectedAuthor));
    }

    // Validate error message for invalid input
    @Then("Invalid | Empty Input Parameters in the Request should be indicated - Update Book")
    public void invalid_empty_input_parameters_in_the_request_should_be_indicated() {
        String errorMessage = response.jsonPath().getString("error");
        assertThat(errorMessage, containsString("Invalid | Empty Input Parameters"));
    }

    // Validate error message for authorization failure
    @Then("the error message should indicate {string} - Update Book")
    public void the_error_message_should_indicate(String expectedMessage) {
        String errorMessage = response.jsonPath().getString("error");
        assertThat(errorMessage, containsString(expectedMessage));
    }

}