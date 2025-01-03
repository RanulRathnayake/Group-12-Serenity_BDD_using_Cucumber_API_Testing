package starter.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DeleteBookSteps {

    private Response response;
    private String baseUrl = "http://localhost:7081";
    private String username;
    private String password;

    @Given("I am authenticated with username {string} and password {string}")
    public void i_am_authenticated_with_username_and_password(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Given("I have a book with ID {int}, title {string}, and author {string}")
    public void I_ave_a_book_with_ID_title_and_author(int id, String title, String author) {

        String jsonBody = String.format("{\"id\": %d, \"title\": \"%s\", \"author\": \"%s\"}", id, title, author);

        // Send the POST request to create a new book
        response = given()
                .auth().basic(username, password)
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post(baseUrl + "/api/books");
    }
    @When("I send a DELETE request to remove the book with id {int}")
    public void i_send_a_delete_request_to_remove_the_book_with_id(Integer id) {
        response = given()
                .auth().basic(username, password)
                .when()
                .delete(baseUrl + "/api/books/" + id);
    }

    @Then("the delete response status code should be {int}")
    public void the_delete_response_status_code_should_be(Integer expectedStatusCode) {
        assertThat(response.statusCode(), equalTo(expectedStatusCode));
    }

    @When("I send a GET request to retrieve the deleted book with id {int}")
    public void i_send_a_get_request_to_retrieve_the_deleted_book_with_id(Integer id) {
        response = given()
                .auth().basic(username, password)
                .when()
                .get(baseUrl + "/api/books/" + id);
    }

    @Then("the book should not be found with status code {int}")
    public void the_book_should_not_be_found_with_status_code(Integer expectedStatusCode) {
        assertThat(response.statusCode(), equalTo(expectedStatusCode));
    }

    @When("I attempt to delete a book with invalid id {int}")
    public void i_attempt_to_delete_a_book_with_invalid_id(Integer invalidId) {
        response = given()
                .auth().basic(username, password)
                .when()
                .delete(baseUrl + "/api/books/" + invalidId);
    }
}