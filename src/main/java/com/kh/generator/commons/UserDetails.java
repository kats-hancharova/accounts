package com.kh.generator.commons;

import org.apache.commons.lang3.RandomStringUtils;

import static com.kh.generator.commons.Property.getProperty;

public class UserDetails {

    private static final int MIN_EMAIL_LENGTH = Integer.parseInt(getProperty("minEmailLength"));
    private static final int MAX_EMAIL_LENGTH = Integer.parseInt(getProperty("maxEmailLength"));
    private static final String EMAIL = RandomStringUtils.randomAlphanumeric(MIN_EMAIL_LENGTH, MAX_EMAIL_LENGTH);
    private static final String PASSWORD = getProperty("password");
    private static final String FIRST_NAME = getProperty("firstName");
    private static final String LAST_NAME = getProperty("lastName");
    private static final String BIRTH_MONTH = getProperty("birthMonth");
    private static final String BIRTH_DAY = getProperty("birthDay");
    private static final String BIRTH_YEAR = getProperty("birthYear");
    private static final String GENDER = getProperty("gender");

    public String getEmail() {
        return EMAIL;
    }

    public String getPassword() {
        return PASSWORD;
    }

    public String getFirstName() {
        return FIRST_NAME;
    }

    public String getLastName() {
        return LAST_NAME;
    }

    public String getBirthMonth() {
        return BIRTH_MONTH;
    }

    public String getBirthDay() {
        return BIRTH_DAY;
    }

    public String getBirthYear() {
        return BIRTH_YEAR;
    }

    public String getGender() {
        return GENDER;
    }

}
