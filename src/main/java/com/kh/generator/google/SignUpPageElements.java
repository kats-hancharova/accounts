package com.kh.generator.google;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static com.kh.generator.common.Property.*;

public class SignUpPageElements {

    private static final String URL = getProperty("signUpPage");

    public SignUpPageElements(WebDriver driver) {
        driver.get(URL);
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "FirstName")
    private WebElement firstName;

    @FindBy(id = "LastName")
    private WebElement lastName;

    @FindBy(id = "GmailAddress")
    private WebElement email;

    @FindBy(id = "Passwd")
    private WebElement password;

    @FindBy(id = "PasswdAgain")
    private WebElement passwordConfirm;

    @FindBy(id = "BirthMonth")
    private WebElement birthMonthDropdown;

    @FindBy(css = "[id='BirthMonth'] [class='goog-menuitem-content']")
    private List<WebElement> birthMonth;

    @FindBy(id = "BirthDay")
    private WebElement birthDay;

    @FindBy(id = "BirthYear")
    private WebElement birthYear;

    @FindBy(id = "Gender")
    private WebElement genderDropdown;

    @FindBy(css = "[id='Gender'] [class='goog-menuitem-content']")
    private List<WebElement> gender;

    @FindBy(id = "submitbutton")
    private WebElement submitButton;

    @FindBy(className = "tos-scroll-button-icon")
    private WebElement scrollDown;

    @FindBy(id = "iagreebutton")
    private WebElement agreeButton;

    public WebElement getFirstName() {
        return firstName;
    }

    public WebElement getLastName() {
        return lastName;
    }

    public WebElement getEmail() {
        return email;
    }

    public WebElement getPassword() {
        return password;
    }

    public WebElement getPasswordConfirm() {
        return passwordConfirm;
    }

    public WebElement getBirthMonthDropdown() {
        return birthMonthDropdown;
    }

    public List<WebElement> getBirthMonth() {
        return birthMonth;
    }

    public WebElement getBirthDay() {
        return birthDay;
    }

    public WebElement getBirthYear() {
        return birthYear;
    }

    public WebElement getGenderDropdown() {
        return genderDropdown;
    }

    public List<WebElement> getGender() {
        return gender;
    }

    public WebElement getSubmitButton() {
        return submitButton;
    }

    public WebElement getScrollDown() {
        return scrollDown;
    }

    public WebElement getAgreeButton() {
        return agreeButton;
    }

}
