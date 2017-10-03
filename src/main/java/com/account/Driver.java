package com.account;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.concurrent.TimeUnit;

import static com.account.Property.getProperty;

public class Driver {

    private static WebDriver driver;

    static {
        String driverLocation = getProperty("driverLocation");
        System.setProperty("webdriver.gecko.driver", driverLocation);
    }

    public Driver() {
        driver = new FirefoxDriver();
    }

    public Driver(FirefoxOptions options) {
        driver = new FirefoxDriver(options);
    }

    public static void setUpDriver(WebDriver driver) {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public static WebDriver getDriver() {
        return driver;
    }

}
