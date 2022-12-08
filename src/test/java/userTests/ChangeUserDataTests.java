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

public class ChangeUserDataTests {

    private UserClient userClient;
    private UserDto userDto;
    private String accessToken;

    int successStatusCode = SC_OK;
    int failedStatusCode = SC_UNAUTHORIZED;
    String failedMessage = "You should be authorised";

    @Before
    public void setUp() {
        this.userClient = new UserClient();
        String email = Faker.instance().internet().emailAddress();
        String password = Faker.instance().internet().password();
        String name = Faker.instance().name().username();
        this.userDto = DataGenerator.generateUserDto(email, password, name);
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("changing user data with authorization")
    public void changeUserDataWithAuthorizationReturnsOk() {
        // Arrange
        accessToken = (userClient.register(userDto)).extract().path("accessToken");
        String newEmail = Faker.instance().internet().emailAddress();
        String newName = Faker.instance().name().username();

        Map<String, String> expectedUserData = new HashMap<>();
        expectedUserData.put("email", newEmail);
        expectedUserData.put("name", newName);

        // Act
        ValidatableResponse changeDataResponse = userClient.patchData(accessToken, newEmail, newName);

        int actualStatusCode = changeDataResponse.extract().statusCode();
        boolean isUserRegistered = changeDataResponse.extract().path("success");
        Map<String, String> actualUserData = changeDataResponse.extract().path("user");

        // Assert
        assertEquals(successStatusCode, actualStatusCode);
        assertTrue(isUserRegistered);
        assertEquals(expectedUserData, actualUserData);
    }

    @Test
    @DisplayName("changing user data without authorization")
    public void changeUserDataWithoutAuthorizationReturnsError() {
        // Arrange
        accessToken = (userClient.register(userDto)).extract().path("accessToken");
        String newEmail = Faker.instance().internet().emailAddress();
        String newName = Faker.instance().name().username();

        // Act
        ValidatableResponse changeDataResponse = userClient.patchData(Faker.instance().lorem().characters(70, true, true), newEmail, newName);
        int actualStatusCode = changeDataResponse.extract().statusCode();
        boolean isUserRegistered = changeDataResponse.extract().path("success");
        String actualMessage = changeDataResponse.extract().path("message");

        // Assert
        assertEquals(failedStatusCode, actualStatusCode);
        assertFalse(isUserRegistered);
        assertEquals(failedMessage, actualMessage);
    }
}

