package ordertests;

import clients.OrderClient;
import clients.UserClient;
import com.github.javafaker.Faker;
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

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CreateOrderTests {
    private UserClient userClient;
    private UserDto userDto;
    private String accessToken;
    private OrderClient orderClient;
    private OrderDto orderDto;

    private final int successStatusCode = SC_OK;
    private final int failedStatusCode = SC_BAD_REQUEST;
    private final String failedMessage = "Ingredient ids must be provided";
    private final int failedInvalidIngredientStatusCode = SC_INTERNAL_SERVER_ERROR;

    @Before
    public void setUp() {
        this.userClient = new UserClient();
        this.orderClient = new OrderClient();
    }

    @After
    public void cleanUp() {
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("create order by authorized user")
    public void createOrderByAuthorizedUserReturnsOk() {
        // Arrange
        String email = Faker.instance().internet().emailAddress();
        String name = Faker.instance().name().username();
        userDto = DataGenerator.generateUserDto(email, Faker.instance().internet().password(), name);
        accessToken = (userClient.registerUser(userDto)).extract().path("accessToken");

        orderDto = new OrderDto(TestUtils.chooseIngredients(orderClient, 3));

        // Act
        ValidatableResponse createResponse = orderClient.createOrder(accessToken, orderDto);

        int actualStatusCode = createResponse.extract().statusCode();
        boolean isOrderCreated = createResponse.extract().path("success");
        String actualOwnerName = createResponse.extract().path("order.owner.name");
        String actualOwnerEmail = createResponse.extract().path("order.owner.email");
        int actualNumber = createResponse.extract().path("order.number");
        int actualPrice = createResponse.extract().path("order.price");

        // Assert
        assertEquals(successStatusCode, actualStatusCode);
        assertTrue(isOrderCreated);
        assertEquals(name, actualOwnerName);
        assertEquals(email, actualOwnerEmail);
        assertNotEquals(0, actualNumber);
        assertNotEquals(0, actualPrice);
    }

    @Test
    @DisplayName("create order by unauthorized user")
    public void createOrderByUnauthorizedUserReturnsOk() {
        // Arrange
        userDto = DataGenerator.generateRandomUserDto();
        accessToken = (userClient.registerUser(userDto)).extract().path("accessToken");

        orderDto = new OrderDto(TestUtils.chooseIngredients(orderClient, 5));

        // Act
        ValidatableResponse createResponse = orderClient.createOrder(orderDto);

        int actualStatusCode = createResponse.extract().statusCode();
        boolean isOrderCreated = createResponse.extract().path("success");
        int actualNumber = createResponse.extract().path("order.number");

        // Assert
        assertEquals(successStatusCode, actualStatusCode);
        assertTrue(isOrderCreated);
        assertNotEquals(0, actualNumber);
    }

    @Test
    @DisplayName("create order with ingredients")
    public void createOrderWithIngredientsReturnsExpectedIngredients() {
        // Arrange
        userDto = DataGenerator.generateRandomUserDto();
        accessToken = (userClient.registerUser(userDto)).extract().path("accessToken");

        int expectedIngredientNumber = 4;
        String[] expectedIngredientIds = TestUtils.chooseIngredients(orderClient, expectedIngredientNumber);
        orderDto = new OrderDto(expectedIngredientIds);

        // Act
        ValidatableResponse createResponse = orderClient.createOrder(accessToken, orderDto);

        int actualStatusCode = createResponse.extract().statusCode();
        boolean isOrderCreated = createResponse.extract().path("success");
        List<Map<String, String>> actualIngredients = createResponse.extract().path("order.ingredients");
        int actualIngredientNumber = actualIngredients.size();
        List<String> actualIngredientIds = actualIngredients
                .stream()
                .map(x -> x.get("_id"))
                .collect(Collectors.toList());
        int actualNumber = createResponse.extract().path("order.number");
        int actualPrice = createResponse.extract().path("order.price");

        // Assert
        assertEquals(successStatusCode, actualStatusCode);
        assertTrue(isOrderCreated);
        assertEquals(expectedIngredientNumber, actualIngredientNumber);
        assertEquals(List.of(expectedIngredientIds), actualIngredientIds);
        assertNotEquals(0, actualNumber);
        assertNotEquals(0, actualPrice);
    }

    @Test
    @DisplayName("create order without ingredients")
    public void createOrderWithoutIngredientsReturnsError() {
        // Arrange
        userDto = DataGenerator.generateRandomUserDto();
        accessToken = (userClient.registerUser(userDto)).extract().path("accessToken");

        orderDto = new OrderDto(TestUtils.chooseIngredients(orderClient, 0));

        // Act
        ValidatableResponse createResponse = orderClient.createOrder(accessToken, orderDto);

        int actualStatusCode = createResponse.extract().statusCode();
        boolean isOrderCreated = createResponse.extract().path("success");
        String actualMessage = createResponse.extract().path("message");

        // Assert
        assertEquals(failedStatusCode, actualStatusCode);
        assertFalse(isOrderCreated);
        assertEquals(failedMessage, actualMessage);
    }

    @Test
    @DisplayName("create order with invalid ingredients")
    public void createOrderWithInvalidIngredientsReturnsError() {
        // Arrange
        userDto = DataGenerator.generateRandomUserDto();
        accessToken = (userClient.registerUser(userDto)).extract().path("accessToken");

        String[] invalidIngredientIds = new String[]{Faker.instance().lorem().characters(24, false, true), Faker.instance().lorem().characters(24, false, true)};
        orderDto = new OrderDto(invalidIngredientIds);

        // Act
        ValidatableResponse createResponse = orderClient.createOrder(accessToken, orderDto);
        int actualStatusCode = createResponse.extract().statusCode();

        // Assert
        assertEquals(failedInvalidIngredientStatusCode, actualStatusCode);
    }
}