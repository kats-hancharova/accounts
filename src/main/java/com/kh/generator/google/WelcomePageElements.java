package com.kh.generator.google;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class WelcomePageElements {

    public WelcomePageElements(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = ".welcome>h1")
    private List<WebElement> welcomeText;

    public List<WebElement> getWelcomeText() {
        return welcomeText;
    }

}
