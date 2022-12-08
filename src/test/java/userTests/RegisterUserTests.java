package userTests;

import clients.UserClient;
import com.github.javafaker.Faker;
import dtos.UserDto;
import generators.DataGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

public class RegisterUserTests {
    private UserClient userClient;
    private UserDto userDto;
    private String accessToken;
    private String email;
    private String name;

    int successStatusCode = SC_OK;
    int failedStatusCode = SC_FORBIDDEN;
    String failedMessage = "User already exists";

    @Before
    public void setUp() {
        this.userClient = new UserClient();
        this.email = Faker.instance().internet().emailAddress();
        String password = Faker.instance().internet().password();
        this.name = Faker.instance().name().username();
        this.userDto = DataGenerator.generateUserDto(email, password, name);
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("registration of unique user")
    public void registerUserWithUniqueDataReturnsOk() {
        // Arrange
        Map<String, String> expectedUserData = new HashMap<>();
        expectedUserData.put("email", email);
        expectedUserData.put("name", name);

        // Act
        ValidatableResponse registerResponse = userClient.register(userDto);

        int actualStatusCode = registerResponse.extract().statusCode();
        boolean isUserRegistered = registerResponse.extract().path("success");
        Map<String, String> actualUserData = registerResponse.extract().path("user");
        accessToken = registerResponse.extract().path("accessToken");
        String refreshToken = registerResponse.extract().path("refreshToken");

        // Assert
        assertEquals(successStatusCode, actualStatusCode);
        assertTrue(isUserRegistered);
        assertEquals(expectedUserData, actualUserData);
        assertNotNull(accessToken);
        assertNotNull(refreshToken);
    }

    @Test
    @DisplayName("registering user who has already been registered")
    public void registerUserWithDuplicateDataReturnsError() {
        // Arrange
        accessToken = (userClient.register(userDto)).extract().path("accessToken");

        // Act
        ValidatableResponse registerResponse = userClient.register(userDto);

        int actualStatusCode = registerResponse.extract().statusCode();
        boolean isUserRegistered = registerResponse.extract().path("success");
        String actualMessage = registerResponse.extract().path("message");

        // Assert
        assertEquals(failedStatusCode, actualStatusCode);
        assertFalse(isUserRegistered);
        assertEquals(failedMessage, actualMessage);
    }
}