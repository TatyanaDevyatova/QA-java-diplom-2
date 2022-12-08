package generators;

import com.github.javafaker.Faker;
import dtos.UserDto;

public class DataGenerator {

    public static UserDto generateUserDto(String email, String password, String name) {
        return new UserDto(email, password, name);
    }

    public static UserDto generateRandomUserDto() {
        return new UserDto(Faker.instance().internet().emailAddress(), Faker.instance().internet().password(), Faker.instance().name().username());
    }
}
