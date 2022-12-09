package generators;

import com.github.javafaker.Faker;
import dtos.UserDto;
import io.qameta.allure.Step;

public class DataGenerator {

    @Step("creating user model")
    public static UserDto generateUserDto(String email, String password, String name) {
        return new UserDto(email, password, name);
    }

    @Step("creating random user model")
    public static UserDto generateRandomUserDto() {
        return new UserDto(Faker.instance().internet().emailAddress(), Faker.instance().internet().password(), Faker.instance().name().username());
    }
}