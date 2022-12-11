package orderTests;

import clients.OrderClient;
import clients.UserClient;
import dtos.OrderDto;
import dtos.UserDto;
import generators.DataGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.TestUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class GetOrdersTests {
    private UserClient userClient;
    private UserDto userDto;
    private String accessToken;
    private OrderClient orderClient;
    private OrderDto orderDto;

    private final int successStatusCode = SC_OK;
    private final int failedStatusCode = SC_UNAUTHORIZED;
    private final String failedMessage = "You should be authorised";

    @Before
    public void setUp() {
        this.userClient = new UserClient();
        this.userDto = DataGenerator.generateRandomUserDto();
        this.orderClient = new OrderClient();
    }

    @After
    public void cleanUp() {
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("get orders by authorized user")
    public void getOrdersByAuthorizedUserReturnsExpectedOrders() {
        // Arrange
        accessToken = (userClient.registerUser(userDto)).extract().path("accessToken");
        orderDto = new OrderDto(TestUtils.chooseIngredients(orderClient, 3));
        int expectedOrderNumber1 = (orderClient.createOrder(accessToken, orderDto)).extract().path("order.number");
        int expectedOrderNumber2 = (orderClient.createOrder(accessToken, orderDto)).extract().path("order.number");

        // Act
        ValidatableResponse getResponse = orderClient.getUserOrders(accessToken);

        int actualStatusCode = getResponse.extract().statusCode();
        boolean isOrderFund = getResponse.extract().path("success");
        List<Map<String, Object>> actualUserOrders = getResponse.extract().path("orders");
        List<Integer> actualOrderNumbers = actualUserOrders.stream().map(x -> (Integer) x.get("number")).collect(Collectors.toList());
        int actualTotal = getResponse.extract().path("total");
        int actualTotalToday = getResponse.extract().path("totalToday");

        // Assert
        assertEquals(successStatusCode, actualStatusCode);
        assertTrue(isOrderFund);
        assertEquals(List.of(expectedOrderNumber1, expectedOrderNumber2), actualOrderNumbers);
        assertNotEquals(0, actualTotal);
        assertNotEquals(0, actualTotalToday);
    }

    @Test
    @DisplayName("get orders by unauthorized user")
    public void getOrdersByUnauthorizedUserReturnsError() {
        // Arrange
        accessToken = (userClient.registerUser(userDto)).extract().path("accessToken");
        orderDto = new OrderDto(TestUtils.chooseIngredients(orderClient, 4));

        // Act
        ValidatableResponse getResponse = orderClient.getUserOrders("");

        int actualStatusCode = getResponse.extract().statusCode();
        boolean isOrderFund = getResponse.extract().path("success");
        String actualMessage = getResponse.extract().path("message");

        // Assert
        assertEquals(failedStatusCode, actualStatusCode);
        assertFalse(isOrderFund);
        assertEquals(failedMessage, actualMessage);
    }
}