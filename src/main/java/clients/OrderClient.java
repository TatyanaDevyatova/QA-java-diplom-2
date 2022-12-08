package clients;

import dtos.OrderDto;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    private static final String CREATE_PATH = "/api/orders";
    private static final String GET_PATH = "/api/orders/all";
    private static final String INDIVIDUAL_GET_PATH = "/api/orders?token=";

    @Step("create order")
    public ValidatableResponse create(OrderDto orderDto) {

        return given()
                .spec(getSpec())
                .body(orderDto)
                .when()
                .post(CREATE_PATH)
                .then();
    }

    @Step("get all orders")
    public ValidatableResponse getAll() {
        return given()
                .spec(getSpec())
                .when()
                .get(GET_PATH)
                .then();
    }

    @Step("get user orders")
    public ValidatableResponse get(String accessToken) {
        return given()
                .spec(getSpec())
                .when()
                .get(INDIVIDUAL_GET_PATH + accessToken)
                .then();
    }
}
