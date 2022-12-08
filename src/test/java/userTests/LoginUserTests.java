package userTests;

import clients.UserClient;
import com.github.javafaker.Faker;
import dtos.UserDto;
import generators.DataGenerator;
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

    int successStatusCode = SC_OK;
    int failedStatusCode = SC_UNAUTHORIZED;
    String failedMessage = "email or password are incorrect";

    @Before
    public void setUp() {
        userClient = new UserClient();
        email = Faker.instance().internet().emailAddress();
        password = Faker.instance().internet().password();
        name = Faker.instance().name().username();
        userDto = DataGenerator.generateUserDto(email, password, name);
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }

    @Test
    public void loginUserWithExistedUniqueDataReturnsOk() {
        // Arrange
        userClient.register(userDto);

        Map<String, String> expectedUserData = new HashMap<>();
        expectedUserData.put("email", email);
        expectedUserData.put("name", name);

        // Act
        ValidatableResponse loginResponse = userClient.login(email, password);

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
    public void loginUserWithInvalidEmailReturnsError() {
        // Arrange
        accessToken = (userClient.register(userDto)).extract().path("accessToken");

        // Act
        ValidatableResponse loginResponse = userClient.login(Faker.instance().internet().emailAddress(), password);
        int actualStatusCode = loginResponse.extract().statusCode();
        String actualMessage = loginResponse.extract().path("message");

        // Assert
        assertEquals(failedStatusCode, actualStatusCode);
        assertEquals(failedMessage, actualMessage);
    }

    @Test
    public void loginUserWithInvalidPasswordReturnsError() {
        // Arrange
        accessToken = (userClient.register(userDto)).extract().path("accessToken");

        // Act
        ValidatableResponse loginResponse = userClient.login(email, Faker.instance().internet().password());
        int actualStatusCode = loginResponse.extract().statusCode();
        String actualMessage = loginResponse.extract().path("message");

        // Assert
        assertEquals(failedStatusCode, actualStatusCode);
        assertEquals(failedMessage, actualMessage);
    }

    @Test
    public void loginUserWithNonExistedDataReturnsError() {
        // Arrange
        accessToken = (userClient.register(userDto)).extract().path("accessToken");

        // Act
        ValidatableResponse loginResponse = userClient.login(Faker.instance().internet().emailAddress(), Faker.instance().internet().password());
        int actualStatusCode = loginResponse.extract().statusCode();
        String actualMessage = loginResponse.extract().path("message");

        // Assert
        assertEquals(failedStatusCode, actualStatusCode);
        assertEquals(failedMessage, actualMessage);
    }
}