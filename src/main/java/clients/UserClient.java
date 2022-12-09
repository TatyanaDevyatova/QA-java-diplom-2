package clients;

import dtos.CredentialsDto;
import dtos.UserDataDto;
import dtos.UserDto;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {
    private static final String REGISTER_USER_PATH = "/api/auth/register";
    private static final String LOGIN_USER_PATH = "/api/auth/login";
    private static final String CHANGE_USER_DATA_PATH = "/api/auth/user";
    private static final String DELETE_USER_PATH = "/api/auth/user";

    @Step("user registration")
    public ValidatableResponse registerUser(UserDto userDto) {
        return given()
                .spec(getSpecification())
                .body(userDto)
                .when()
                .post(REGISTER_USER_PATH)
                .then();
    }

    @Step("user authorization")
    public ValidatableResponse loginUser(String email, String password) {
        CredentialsDto credentialsDto = new CredentialsDto(email, password);
        return given()
                .spec(getSpecification())
                .body(credentialsDto)
                .when()
                .post(LOGIN_USER_PATH)
                .then();
    }

    @Step("changing user data")
    public ValidatableResponse changeUserData(String accessToken, String email, String name) {
        UserDataDto userDataDto = new UserDataDto(accessToken, email, name);
        return given()
                .spec(getSpecification())
                .headers("authorization", accessToken)
                .body(userDataDto)
                .when()
                .patch(CHANGE_USER_DATA_PATH)
                .then();
    }

    @Step("deleting user")
    public void deleteUser(String accessToken) {
        given()
                .spec(getSpecification())
                .headers("authorization", accessToken)
                .when()
                .delete(DELETE_USER_PATH)
                .then();
    }
}