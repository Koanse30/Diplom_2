package ru.praktikum;

import org.apache.commons.lang3.RandomStringUtils;

public class User {

    private String email;
    private String password;
    private String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User() {}

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public User userGenerator() {
        return new User(RandomStringUtils.randomAlphanumeric(5,8) + "@gmail.com",
                RandomStringUtils.randomAlphanumeric(5,8),
                RandomStringUtils.randomAlphanumeric(5,8));
    }

    public User withoutEmail() {
        return new User("",
                RandomStringUtils.randomAlphanumeric(5,8),
                RandomStringUtils.randomAlphanumeric(5,8));
    }

    public User withoutPassword() {
        return new User(RandomStringUtils.randomAlphanumeric(5,8) + "@gmail.com",
                "",
                RandomStringUtils.randomAlphanumeric(5,8));
    }

    public User withoutName() {
        return new User(RandomStringUtils.randomAlphanumeric(5,8) + "@gmail.com",
                RandomStringUtils.randomAlphanumeric(5,8),
                "");
    }
}
