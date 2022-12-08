package clients;

import dtos.CredentialsDto;
import dtos.UserDataDto;
import dtos.UserDto;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.BeforeClass;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {
    private static final String REGISTRATION_PATH = "/api/auth/register";
    private static final String AUTHORIZATION_PATH = "/api/auth/login";
    private static final String DATA_PATH = "/api/auth/user";
    private static final String DELETE_PATH = "/api/auth/user";

    @Step("boria")
    public ValidatableResponse register(UserDto userDto) {
        return given()
                .spec(getSpec())
                .body(userDto)
                .when()
                .post(REGISTRATION_PATH)
                .then();
    }

    public ValidatableResponse login(CredentialsDto credentialsDto) {
        return given()
                .spec(getSpec())
                .body(credentialsDto)
                .when()
                .post(AUTHORIZATION_PATH)
                .then();
    }

    public ValidatableResponse login(String login, String password) {
        CredentialsDto credentialsDto = new CredentialsDto(login, password);
        return given()
                .spec(getSpec())
                .body(credentialsDto)
                .when()
                .post(AUTHORIZATION_PATH)
                .then();
    }

    public ValidatableResponse getData(String accessToken) {
        return given()
                .spec(getSpec())
                .auth().oauth2(accessToken)
//                .body(accessToken)
                .when()
                .get(DATA_PATH)
                .then();
    }

    public ValidatableResponse patchData(UserDataDto userDataDto) {
        return given()
                .spec(getSpec())
                .body(userDataDto)
                .when()
                .patch(DATA_PATH)
                .then();
    }

    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getSpec())
                .headers("authorization", accessToken)
                .when()
                .delete(DELETE_PATH)
                .then();
    }
}
