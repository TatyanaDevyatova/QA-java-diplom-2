package userTests;

import clients.UserClient;
import com.github.javafaker.Faker;
import dtos.UserDto;
import generators.DataGenerator;
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
    private UserDto userDto;
    private String email;
    private String password;
    private String name;
    private int expectedStatusCode;
    private String expectedMessage;

    static int failedStatusCode = SC_FORBIDDEN;
    static String failedMessage = "Email, password and name are required fields";

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

    @Parameterized.Parameters
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
    public void registerUserWithIncompleteDataReturnsError() {
        // Arrange
        userDto = DataGenerator.generateUserDto(email, password, name);

        // Act
        ValidatableResponse registerResponse = userClient.register(userDto);

        int actualStatusCode = registerResponse.extract().statusCode();
        boolean isUserRegistered = registerResponse.extract().path("success");
        String actualMessage = registerResponse.extract().path("message");

        // Assert
        assertEquals(expectedStatusCode, actualStatusCode);
        assertFalse(isUserRegistered);
        assertEquals(expectedMessage, actualMessage);
    }
}
