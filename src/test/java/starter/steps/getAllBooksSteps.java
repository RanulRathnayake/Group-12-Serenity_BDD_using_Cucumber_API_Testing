package starter.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class getAllBooksSteps {

    private Response response;
    private String baseUrl = "http://localhost:7081";
    private String username;
    private String password;

    // Given step - login
    @Given("I am logged in with the username {string} and password {string} - GET")
    public void i_am_logged_in_with_the_username_and_password(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Given step - not logged in
    @Given("I am not logged in - GET")
    public void i_am_not_logged_in() {
        this.username = null;
        this.password = null;
    }

    // When step - request to get all books
    @When("I request to get all books - GET")
    public void i_request_to_get_all_books() {
        if (username != null && password != null) {
            response = given()
                    .auth().basic(username, password)
                    .when()
                    .get(baseUrl + "/api/books");
        } else {
            response = given()
                    .when()
                    .get(baseUrl + "/api/books");
        }
    }

    // Then step - validate the response contains a list of books
    @And("the response should contain a list of books")
    public void the_response_should_contain_a_list_of_books() {
        assertThat(response.jsonPath().getList(""), is(not(empty())));
    }

    // Then step - validate error message for missing authentication
    @And("the response should indicate that authentication is required")
    public void the_response_should_indicate_that_authentication_is_required() {
        assertThat(response.statusCode(), equalTo(401));
        String responseBody = response.body().asString();
        if (responseBody.isEmpty()) {
            System.out.println("Response body is empty. Authentication is required.");
        } else {
            String errorMessage = response.jsonPath().getString("message");
            assertThat(errorMessage, containsString("Authentication is required"));
        }
    }

    // Given step - successfully fetch books with valid credentials
    @And("I request to get all books successfully")
    public void i_request_to_get_all_books_successfully() {
        response = given()
                .auth().basic(username, password)
                .when()
                .get(baseUrl + "/api/books");
        assertThat(response.statusCode(), equalTo(200));
        assertThat(response.jsonPath().getList(""), is(not(empty())));
    }

    // When step - remove the password and request again
    @And("I remove the password and request to get all books again")
    public void i_remove_the_password_and_request_to_get_all_books_again() {
        this.password = null; // Remove the password
        if (username != null && password == null) {
            response = given()
                    .when()
                    .get(baseUrl + "/api/books");
        } else if (username != null && password != null) {
            response = given()
                    .auth().basic(username, password)
                    .when()
                    .get(baseUrl + "/api/books");
        }
    }

    // Then step - validate response is unauthorized for missing password
    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedStatusCode) {
        assertThat(response.statusCode(), equalTo(expectedStatusCode));
    }

}
