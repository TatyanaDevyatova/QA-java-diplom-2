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

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class LoginUserTests {
    private UserClient userClient;
    private UserDto userDto;
    private String accessToken;
    private String email;
    private String password;
    private String name;

    private final int successStatusCode = SC_OK;
    private final int failedStatusCode = SC_UNAUTHORIZED;
    private final String failedMessage = "email or password are incorrect";

    @Before
    public void setUp() {
        this.userClient = new UserClient();
        this.email = Faker.instance().internet().emailAddress();
        this.password = Faker.instance().internet().password();
        this.name = Faker.instance().name().username();
        this.userDto = DataGenerator.generateUserDto(email, password, name);
    }

    @After
    public void cleanUp() {
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("authorization of existing user")
    public void loginUserWithExistedUniqueDataReturnsOk() {
        // Arrange
        userClient.registerUser(userDto);

        Map<String, String> expectedUserData = new HashMap<>();
        expectedUserData.put("email", email);
        expectedUserData.put("name", name);

        // Act
        ValidatableResponse loginResponse = userClient.loginUser(email, password);

        int actualStatusCode = loginResponse.extract().statusCode();
        boolean isUserRegistered = loginResponse.extract().path("success");
        Map<String, String> actualUserData = loginResponse.extract().path("user");
        accessToken = loginResponse.extract().path("accessToken");
        String refreshToken = loginResponse.extract().path("refreshToken");

        // Assert
        assertEquals(successStatusCode, actualStatusCode);
        assertTrue(isUserRegistered);
        assertEquals(expectedUserData, actualUserData);
        assertNotNull(accessToken);
        assertNotNull(refreshToken);
    }

    @Test
    @DisplayName("user authorization with wrong email")
    public void loginUserWithInvalidEmailReturnsError() {
        // Arrange
        accessToken = (userClient.registerUser(userDto)).extract().path("accessToken");

        // Act
        ValidatableResponse loginResponse = userClient.loginUser(Faker.instance().internet().emailAddress(), password);
        int actualStatusCode = loginResponse.extract().statusCode();
        boolean isUserRegistered = loginResponse.extract().path("success");
        String actualMessage = loginResponse.extract().path("message");

        // Assert
        assertEquals(failedStatusCode, actualStatusCode);
        assertFalse(isUserRegistered);
        assertEquals(failedMessage, actualMessage);
    }

    @Test
    @DisplayName("user authorization with wrong password")
    public void loginUserWithInvalidPasswordReturnsError() {
        // Arrange
        accessToken = (userClient.registerUser(userDto)).extract().path("accessToken");

        // Act
        ValidatableResponse loginResponse = userClient.loginUser(email, Faker.instance().internet().password());
        int actualStatusCode = loginResponse.extract().statusCode();
        boolean isUserRegistered = loginResponse.extract().path("success");
        String actualMessage = loginResponse.extract().path("message");

        // Assert
        assertEquals(failedStatusCode, actualStatusCode);
        assertFalse(isUserRegistered);
        assertEquals(failedMessage, actualMessage);
    }

    @Test
    @DisplayName("user authorization with non-existent data")
    public void loginUserWithNonExistedDataReturnsError() {
        // Arrange
        accessToken = (userClient.registerUser(userDto)).extract().path("accessToken");

        // Act
        ValidatableResponse loginResponse = userClient.loginUser(Faker.instance().internet().emailAddress(), Faker.instance().internet().password());
        int actualStatusCode = loginResponse.extract().statusCode();
        boolean isUserRegistered = loginResponse.extract().path("success");
        String actualMessage = loginResponse.extract().path("message");

        // Assert
        assertEquals(failedStatusCode, actualStatusCode);
        assertFalse(isUserRegistered);
        assertEquals(failedMessage, actualMessage);
    }
}