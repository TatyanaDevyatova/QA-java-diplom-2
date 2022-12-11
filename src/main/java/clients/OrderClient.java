package clients;

import dtos.OrderDto;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    private static final String CREATE_ORDER_PATH = "/api/orders";
    private static final String GET_USER_ORDERS_PATH = "/api/orders?token=";

    @Step("creating order by authorized user")
    public ValidatableResponse createOrder(String accessToken, OrderDto orderDto) {
        return given()
                .spec(getSpecification())
                .headers("authorization", accessToken)
                .body(orderDto)
                .when()
                .post(CREATE_ORDER_PATH)
                .then();
    }

    @Step("creating order by unauthorized user")
    public ValidatableResponse createOrder(OrderDto orderDto) {
        return given()
                .spec(getSpecification())
                .body(orderDto)
                .when()
                .post(CREATE_ORDER_PATH)
                .then();
    }

    @Step("getting user orders")
    public ValidatableResponse getUserOrders(String accessToken) {
        return given()
                .spec(getSpecification())
                .headers("authorization", accessToken)
                .when()
                .get(GET_USER_ORDERS_PATH)
                .then();
    }
}