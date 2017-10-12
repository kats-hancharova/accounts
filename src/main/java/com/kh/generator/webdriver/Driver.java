package com.kh.generator.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.kh.generator.commons.Property.getProperty;

public class Driver {

    private WebDriver driver;

    static {
        System.setProperty("webdriver.gecko.driver", getProperty("driverLocation"));
    }

    public Driver() {
        driver = new FirefoxDriver();
        setUpDriver();
    }

    public Driver(FirefoxOptions options) {
        driver = new FirefoxDriver(options);
        setUpDriver();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setUpDriver() {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public void getElementFromList(List<WebElement> elements, String value) {
        for (WebElement element : elements) {
            if (element.getText().contains(value)) {
                element.click();
            }
        }
    }

    public void waitForElement(WebElement element) {
        (new WebDriverWait(driver, 5))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

}
