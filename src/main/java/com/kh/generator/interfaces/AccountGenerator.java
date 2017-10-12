package com.kh.generator.interfaces;

import com.kh.generator.commons.UserDetails;
import com.kh.generator.google.SignUpPageElements;
import com.kh.generator.webdriver.Driver;

public interface AccountGenerator {

    void fillOutSignUpForm(Driver driver, SignUpPageElements pageElements, UserDetails userDetails)
            throws InterruptedException;

}
