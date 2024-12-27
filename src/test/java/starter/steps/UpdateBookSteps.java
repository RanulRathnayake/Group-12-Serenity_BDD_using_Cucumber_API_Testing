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

    // login
    @Given("I am logged in with the username {string} and password {string}")
    public void i_am_logged_in_with_the_username_and_password(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // create book if it doesn't exist
    @Given("I have a book with ID {string}, title {string}, and author {string}")
    public void i_have_a_book_with_id_title_and_author(String bookId, String title, String author) {
        // Check if the book exists, if not create it
        Response getResponse = given()
                .auth().basic(username, password)
                .when()
                .get(baseUrl + "/api/books/" + bookId);

        if (getResponse.statusCode() == 404) {
            String jsonBody = String.format("{\"id\": \"%s\", \"title\": \"%s\", \"author\": \"%s\"}", bookId, title, author);
            given()
                    .auth().basic(username, password)
                    .contentType("application/json")
                    .body(jsonBody)
                    .when()
                    .post(baseUrl + "/api/books");
        }
    }

    // update book details
    @When("I update the book with ID {string} to title {string} and author {string}")
    public void i_update_the_book_with_id_to_title_and_author(String bookId, String title, String author) {
        expectedTitle = title;
        expectedAuthor = author;

        String jsonBody = String.format("{\"id\": \"%s\", \"title\": \"%s\", \"author\": \"%s\"}", bookId, title, author);

        response = given()
                .auth().basic(username, password)
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .put(baseUrl + "/api/books/" + bookId);
    }

    // validate response status code
    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedStatusCode) {
        assertThat(response.statusCode(), equalTo(expectedStatusCode));
    }

    // validate updated book details
    @Then("the book should be updated with the new details")
    public void the_book_should_be_updated_with_the_new_details() {
        String updatedTitle = response.jsonPath().getString("title");
        String updatedAuthor = response.jsonPath().getString("author");

        assertThat(updatedTitle, equalTo(expectedTitle));
        assertThat(updatedAuthor, equalTo(expectedAuthor));
    }
}
