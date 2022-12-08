package userTests;

import clients.UserClient;
import com.github.javafaker.Faker;
import dtos.UserDto;
import generators.DataGenerator;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

//@RunWith(Parameterized.class)
public class LoginUserParametrizedTest {
//    private UserClient userClient;
//    private UserDto userDto;
//    private String accessToken;
//    private String email;
//    private String password;
//    private int expectedStatusCode;
//    private String expectedMessage;
//
//    static int failedStatusCode = SC_UNAUTHORIZED;
//    //static String failedMessage = "Email, password and name are required fields";
//    static String failedMessage = "email or password are incorrect";
//
//    public LoginUserParametrizedTest(String email, String password, int expectedStatusCode, String expectedMessage) {
//        this.email = email;
//        this.password = password;
//        this.expectedStatusCode = expectedStatusCode;
//        this.expectedMessage = expectedMessage;
//    }
//
//    @Before
//    public void setUp() {
//        this.userClient = new UserClient();
//    }
//
//    @After
//    public void cleanUp() {
//        userClient.delete(accessToken);
//    }
//
//    @Parameterized.Parameters
//    public static Object[][] getTestData() {
//        return new Object[][]{
//                {Faker.instance().internet().emailAddress(), "", failedStatusCode, failedMessage},
//                {"", Faker.instance().internet().password(), failedStatusCode, failedMessage},
//                {"", "", failedStatusCode, failedMessage},
//
////                {Faker.instance().internet().emailAddress(), Faker.instance().internet().password(), "", failedStatusCode, failedMessage},
////                {Faker.instance().internet().emailAddress(), "", Faker.instance().name().username(), failedStatusCode, failedMessage},
////                {"", Faker.instance().internet().password(), Faker.instance().name().username(), failedStatusCode, failedMessage},
////                {Faker.instance().internet().emailAddress(), "", "", failedStatusCode, failedMessage},
////                {"", Faker.instance().internet().password(), "", failedStatusCode, failedMessage},
////                {"", "", Faker.instance().name().username(), failedStatusCode, failedMessage},
////                {"", "", "", failedStatusCode, failedMessage},
//
//        };
//    }
//
////    @Test
////    public void registerUserWithInvalidDataReturnsError() {
////        // Arrange
////        userDto = DataGenerator.generateUserDto(email, password, Faker.instance().name().username());
////        accessToken = (userClient.login(email, password)).extract().path("accessToken");
////        var registerResult = userClient.register(userDto);
////        var stat = registerResult.extract().statusCode();
////
////        // Act
////        ValidatableResponse loginResponse = userClient.login(email, password);
////
////        int actualStatusCode = loginResponse.extract().statusCode();
////        boolean isUserRegistered = loginResponse.extract().path("success");
////        String actualMessage = loginResponse.extract().path("message");
////
////        // Assert
////        assertEquals(expectedStatusCode, actualStatusCode);
////        assertFalse(isUserRegistered);
////        assertEquals(expectedMessage, actualMessage);
////    }


}
