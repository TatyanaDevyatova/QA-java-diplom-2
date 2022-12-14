package clients;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Client {
    private static final String BASE_PATH = "https://stellarburgers.nomoreparties.site";
    private static final String GET_INGREDIENTS_PATH = "/api/ingredients";

    protected RequestSpecification getSpecification() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_PATH)
                .build();
    }

    @Step("getting list of available ingredients")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(getSpecification())
                .when()
                .get(GET_INGREDIENTS_PATH)
                .then();
    }
}