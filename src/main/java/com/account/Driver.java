package com.account;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.account.Property.getProperty;

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

    public void setUpDriver() {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public void goToUrl(String url) {
        driver.get(getProperty(url));
    }

    public WebElement getElementById(String id) {
        return driver.findElement(By.id(id));
    }

    public WebElement getElementBySelector(String selector) {
        return driver.findElement(By.cssSelector(selector));
    }

    public WebElement getElementByClass(String className) {
        return driver.findElement(By.className(className));
    }

    public List<WebElement> getElementsBySelector(String selector) {
        return driver.findElements(By.cssSelector(selector));
    }

    public void selectElementByValue(By locator, String value) {
        List<WebElement> elements = driver.findElements(locator);
        for (WebElement element : elements) {
            if (element.getText().contains(value)) {
                element.click();
            }
        }
    }

    public void waitForElement(String className) {
        (new WebDriverWait(driver, 5))
                .until(ExpectedConditions.elementToBeClickable(By.className(className)));
    }

    public void toActiveElement() {
        driver.switchTo().activeElement();
    }

    public void close() {
        driver.close();
    }
}
