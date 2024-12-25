package starter.apis;

import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

public class CreateNewBook {

    private static final EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
    private static final String BASE_URL = environmentVariables.getProperty("api.base.url");

    public static void createBook(Object payload) {
        SerenityRest.given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(payload)
                .post("/api/books");
    }
}
