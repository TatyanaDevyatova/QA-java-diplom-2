package usertests;

import clients.UserClient;
import com.github.javafaker.Faker;
import dtos.UserDto;
import generators.DataGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class RegisterUserParametrizedTest {
    private UserClient userClient;
    private final String email;
    private final String password;
    private final String name;
    private final int expectedStatusCode;
    private final String expectedMessage;

    private static final int failedStatusCode = SC_FORBIDDEN;
    private static final String failedMessage = "Email, password and name are required fields";

    public RegisterUserParametrizedTest(String email, String password, String name, int expectedStatusCode, String expectedMessage) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedMessage = expectedMessage;
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0} | {1} | {2} | {3} | {4}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {Faker.instance().internet().emailAddress(), Faker.instance().internet().password(), "", failedStatusCode, failedMessage},
                {Faker.instance().internet().emailAddress(), "", Faker.instance().name().username(), failedStatusCode, failedMessage},
                {"", Faker.instance().internet().password(), Faker.instance().name().username(), failedStatusCode, failedMessage},
                {Faker.instance().internet().emailAddress(), "", "", failedStatusCode, failedMessage},
                {"", Faker.instance().internet().password(), "", failedStatusCode, failedMessage},
                {"", "", Faker.instance().name().username(), failedStatusCode, failedMessage},
                {"", "", "", failedStatusCode, failedMessage},
        };
    }

    @Test
    @DisplayName("user registration with incomplete data")
    public void registerUserWithIncompleteDataReturnsError() {
        // Arrange
        UserDto userDto = DataGenerator.generateUserDto(email, password, name);

        // Act
        ValidatableResponse registerResponse = userClient.registerUser(userDto);

        int actualStatusCode = registerResponse.extract().statusCode();
        boolean isUserRegistered = registerResponse.extract().path("success");
        String actualMessage = registerResponse.extract().path("message");

        // Assert
        assertEquals(expectedStatusCode, actualStatusCode);
        assertFalse(isUserRegistered);
        assertEquals(expectedMessage, actualMessage);
    }
}