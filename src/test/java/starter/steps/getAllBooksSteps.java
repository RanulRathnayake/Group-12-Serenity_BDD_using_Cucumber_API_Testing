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
    @When("I request to get all books")
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

    // Then step - validate response status code for successful get
    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedStatusCode) {
        assertThat(response.statusCode(), equalTo(expectedStatusCode));
    }

    // Then step - validate the response contains a list of books
    @And("the response should contain a list of books")
    public void the_response_should_contain_a_list_of_books() {
        assertThat(response.jsonPath().getList(""), is(not(empty())));
    }

    // Then step - validate error message for missing authentication
    @And("the response should indicate that authentication is required")
    public void the_response_should_indicate_that_authentication_is_required() {
        // Ensure the response has a 401 status code first
        assertThat(response.statusCode(), equalTo(401));

        // Check if the response body is empty
        String responseBody = response.body().asString();
        if (responseBody.isEmpty()) {
            // If the response body is empty, handle accordingly (you can add more validation here)
            System.out.println("Response body is empty. Authentication is required.");
        } else {
            // If the body is not empty, parse the error message
            String errorMessage = response.jsonPath().getString("message");
            assertThat(errorMessage, containsString("Authentication is required"));
        }
    }
}
