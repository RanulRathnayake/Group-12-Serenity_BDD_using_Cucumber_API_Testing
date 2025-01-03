package starter.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetABookSteps {

    private Response response;
    private String baseUrl = "http://localhost:7081";
    private String username;
    private String password;
    private String bookId;

    @Given("I am logged in with username {string} and password {string}")
    public void i_am_logged_in_with_username_and_password(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Given("I am not logged in")
    public void i_am_not_logged_in() {
        this.username = null;
        this.password = null;
    }

    @And("the book ID is {string}")
    public void the_book_id_is(String bookId) {
        this.bookId = bookId;
    }

    @When("I create a book with id {int} title {string} and author {string}")
    public void I_create_a_book_with_title_and_author(int id, String title, String author) {
        String jsonBody = String.format("{\"id\": %d, \"title\": \"%s\", \"author\": \"%s\"}", id, title, author);
        System.out.println(jsonBody);
        response = given()
                .auth().basic(username, password)
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post(baseUrl + "/api/books");
    }

    @And("the book should be created with the id {int} title {string} and author {string}")
    public void the_book_should_be_created_with_title_and_author(int id, String title, String author) {
        int createdId = response.jsonPath().getInt("id");
        String createdTitle = response.jsonPath().getString("title");
        String createdAuthor = response.jsonPath().getString("author");

        assertThat(createdId, equalTo(id));
        assertThat(createdTitle, equalTo(title));
        assertThat(createdAuthor, equalTo(author));
    }


    @When("I request to get the book")
    public void i_request_to_get_the_book() {
        if (username != null && password != null) {
            response = given()
                    .auth().basic(username, password)
                    .when()
                    .get(baseUrl + "/api/books/" + bookId);
        } else {
            response = given()
                    .when()
                    .get(baseUrl + "/api/books/" + bookId);
        }
    }

    @Then("The response status code should be {int}")
    public void The_response_status_code_should_be(Integer expectedStatusCode) {
        assertThat(response.statusCode(), equalTo(expectedStatusCode));
    }

    @And("the response should contain the book details")
    public void the_response_should_contain_the_book_details() {
        assertThat(response.jsonPath().getString("id"), equalTo(bookId));
    }

    @And("response should indicate that authentication is required")
    public void response_should_indicate_that_authentication_is_required() {
        assertThat(response.statusCode(), equalTo(401));

        String responseBody = response.body().asString();
        if (responseBody.isEmpty()) {
            System.out.println("Response body is empty. Authentication is required.");
        } else {
            String errorMessage = response.jsonPath().getString("message");
            assertThat(errorMessage, containsString("Authentication is required"));
        }
    }
}