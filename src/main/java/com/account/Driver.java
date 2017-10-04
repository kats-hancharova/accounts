package com.account;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.util.List;
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

    public static WebElement getElementById(String id) {
        return driver.findElement(By.id(id));
    }

    public static WebElement getElementBySelector(String selector) {
        return driver.findElement(By.cssSelector(selector));
    }

    public static void selectElementByValue(By locator, String value) {
        List<WebElement> elements = getDriver().findElements(locator);
        for (WebElement element : elements) {
            if (element.getText().contains(value)) {
                element.click();
            }
        }
    }

    public static FirefoxProfile setUpProxyProfile(String ip, Integer port) {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("network.proxy.type", 1);
        profile.setPreference("network.proxy.http", ip);
        profile.setPreference("network.proxy.http_port", port);
        profile.setPreference("network.proxy.ftp", ip);
        profile.setPreference("network.proxy.ftp_port", port);
        profile.setPreference("network.proxy.socks", ip);
        profile.setPreference("network.proxy.socks_port", port);
        profile.setPreference("network.proxy.ssl", ip);
        profile.setPreference("network.proxy.ssl_port", port);
        return profile;
    }
}
