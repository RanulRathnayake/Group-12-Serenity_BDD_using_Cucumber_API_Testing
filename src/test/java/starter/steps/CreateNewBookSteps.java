package starter.steps;

import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

public class CreateNewBookSteps {

    private Response response;

    private Map<String, Object> createBookPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", "The Great Gatsby");
        payload.put("author", "F. Scott Fitzgerald");
        return payload;
    }

    @Step("I have a valid book payload")
    public void iHaveAValidBookPayload() {
        SerenityRest.given()
                .contentType("application/json")
                .body(createBookPayload());
    }

    @Step("I send a POST request to {0}")
    public void iSendAPostRequestTo(String endpoint) {
        response = SerenityRest.given()
                .contentType("application/json")
                .body(createBookPayload())
                .post(endpoint);
    }

    @Step("The response status should be {0}")
    public void theResponseStatusShouldBe(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Step("The response should contain the book details")
    public void theResponseShouldContainTheBookDetails() {
        Map<String, Object> expectedData = createBookPayload();
        Assert.assertEquals(expectedData.get("title"), response.jsonPath().getString("title"));
        Assert.assertEquals(expectedData.get("author"), response.jsonPath().getString("author"));
    }
}
