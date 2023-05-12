package ru.praktikum;

import org.apache.commons.lang3.RandomStringUtils;

public class UserUpdate {

    private String email;
    private String name;

    public UserUpdate(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public static UserUpdate updateEmail(User user) {
        return new UserUpdate(RandomStringUtils.randomAlphanumeric(5,8).toLowerCase() + "@gmail.com",
                user.getName());
    }

    public static UserUpdate updateName(User user) {
        return new UserUpdate(user.getEmail(),
                RandomStringUtils.randomAlphanumeric(5,8));
    }

    public static UserUpdate updateUserAll(User user) {
        return new UserUpdate(RandomStringUtils.randomAlphanumeric(5,8).toLowerCase() + "@gmail.com",
                RandomStringUtils.randomAlphanumeric(5,8));
    }
}
