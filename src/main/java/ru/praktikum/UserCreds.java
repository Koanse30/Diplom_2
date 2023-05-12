package ru.praktikum;

import org.apache.commons.lang3.RandomStringUtils;

public class UserCreds {

    private String email;
    private String password;

    public UserCreds(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserCreds credsFrom(User user) {
        return new UserCreds(user.getEmail(), user.getPassword());
    }

    public static UserCreds credsWithIncorrectEmailFrom(User user) {
        return new UserCreds(RandomStringUtils.randomAlphanumeric(5,8).toLowerCase() + "@gmail.com", user.getPassword());
    }

    public static UserCreds credsWithIncorrectPasswordFrom(User user) {
        return new UserCreds(user.getEmail(), RandomStringUtils.randomAlphanumeric(5,8));
    }
}
