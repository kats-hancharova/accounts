package com.account;

public interface AccountGenerator {
    void goToUrl(String url);
    void fillOutSignUpForm(String email) throws InterruptedException;
}
