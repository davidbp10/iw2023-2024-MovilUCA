package es.uca.iw.telefonuca.user;

import com.github.javafaker.Faker;

import es.uca.iw.telefonuca.user.domain.User;

public class ObjectMother {

    private static final Faker faker = new Faker();

    public static User createTestUser() {
        User testUser = new User();
        testUser.setUsername(faker.name().username());
        testUser.setName(faker.name().firstName());
        testUser.setSurname(faker.name().lastName());
        testUser.setEmail(faker.internet().emailAddress());
        testUser.setPassword("password");
        return testUser;
    }

    public static User createTestUser(String username) {

        User testUser = new User();
        testUser.setUsername(username);
        testUser.setName(faker.name().firstName());
        testUser.setSurname(faker.name().lastName());
        testUser.setEmail(faker.internet().emailAddress());
        testUser.setPassword("password");
        return testUser;

    }
}
