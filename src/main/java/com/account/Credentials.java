package com.account;

import static com.account.Property.getProperty;

public class Credentials {

    private String email;
    private final String password = getProperty("password");


    public Credentials(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
