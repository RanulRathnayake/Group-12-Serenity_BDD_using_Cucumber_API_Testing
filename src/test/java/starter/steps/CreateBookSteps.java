package starter.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateBookSteps {

    private Response response;
    private String baseUrl = "http://localhost:7081";
    private String username;
    private String password;

    // Given step - login
    @Given("I am logged in with the username {string} and password {string}")
    public void i_am_logged_in_with_the_username_and_password(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // When step - create a new book with valid data
    @When("I create a new book with id {int} title {string} and author {string}")
    public void i_create_a_new_book_with_title_and_author(int id, String title, String author) {
        // Prepare the request body
        String jsonBody = String.format("{\"id\": %d, \"title\": \"%s\", \"author\": \"%s\"}", id, title, author);

        // Send the POST request to create a new book
        response = given()
                .auth().basic(username, password)
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post(baseUrl + "/api/books");
    }

    // Then step - validate response status code for valid book creation
    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedStatusCode) {
        assertThat(response.statusCode(), equalTo(expectedStatusCode));
    }

    // Then step - validate the created book details
    @And("the book should be created with id {int} title {string} and author {string}")
    public void the_book_should_be_created_with_title_and_author(int id, String title, String author) {
        // Validate the response body contains correct id, title and author
        int createdId = response.jsonPath().getInt("id");
        String createdTitle = response.jsonPath().getString("title");
        String createdAuthor = response.jsonPath().getString("author");

        assertThat(createdId, equalTo(id));
        assertThat(createdTitle, equalTo(title));
        assertThat(createdAuthor, equalTo(author));
    }

    // When step - attempt to create a book with missing title
    @When("I attempt to create a book with id {int} and author {string} but missing title {string}")
    public void i_attempt_to_create_a_book_with_missing_title_and_author(int id, String author, String title) {
        // Prepare the request body with missing title
        String jsonBody = String.format("{\"id\": %d, \"title\": \"%s\", \"author\": \"%s\"}", id, title, author);

        // Send the POST request to create a new book
        response = given()
                .auth().basic(username, password)
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post(baseUrl + "/api/books");
    }

    // Then step - validate response status code for missing title
    @Then("the response status code should be {int} for missing title")
    public void the_response_status_code_should_be_400_for_missing_title(int statuscode) {
        assertThat(response.statusCode(), equalTo(statuscode));
    }
//
//    // Then step - validate error message for missing title
//    @Then("the error message should indicate missing title")
//    public void the_error_message_should_indicate_missing_title() {
//        String errorMessage = response.jsonPath().getString("message");
//        assertThat(errorMessage, containsString("Invalid | Empty Input Parameters in the Request"));
//    }
}
