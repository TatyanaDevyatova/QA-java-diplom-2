package clients;

import dtos.CredentialsDto;
import dtos.UserDataDto;
import dtos.UserDto;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {
    private static final String REGISTRATION_PATH = "/api/auth/register";
    private static final String AUTHORIZATION_PATH = "/api/auth/login";
    private static final String DATA_PATH = "/api/auth/user";
    private static final String DELETE_PATH = "/api/auth/user";

    @Step("user registration")
    public ValidatableResponse register(UserDto userDto) {
        return given()
                .spec(getSpec())
                .body(userDto)
                .when()
                .post(REGISTRATION_PATH)
                .then();
    }

    @Step("user authorization")
    public ValidatableResponse login(CredentialsDto credentialsDto) {
        return given()
                .spec(getSpec())
                .body(credentialsDto)
                .when()
                .post(AUTHORIZATION_PATH)
                .then();
    }

    @Step("user authorization")
    public ValidatableResponse login(String email, String password) {
        CredentialsDto credentialsDto = new CredentialsDto(email, password);
        return given()
                .spec(getSpec())
                .body(credentialsDto)
                .when()
                .post(AUTHORIZATION_PATH)
                .then();
    }

    @Step("getting user data")
    public ValidatableResponse getData(String accessToken) {
        return given()
                .spec(getSpec())
                .headers("authorization", accessToken)
                .when().log().all()
                .get(DATA_PATH)
                .then().log().all();
    }

    @Step("changing user data")
    public ValidatableResponse patchData(String accessToken, String email, String name) {
        UserDataDto userDataDto = new UserDataDto(accessToken, email, name);
        return given()
                .spec(getSpec())
                .headers("authorization", accessToken)
                .body(userDataDto)
                .when()
                .patch(DATA_PATH)
                .then();
    }

    @Step("deleting user")
    public void delete(String accessToken) {
        given()
                .spec(getSpec())
                .headers("authorization", accessToken)
                .when()
                .delete(DELETE_PATH)
                .then();
    }
}
