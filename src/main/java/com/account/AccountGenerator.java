package com.account;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.account.Property.*;

public class AccountGenerator {

    private static final byte MIN_EMAIL_LENGTH = Byte.parseByte(getProperty("minEmailLength"));
    private static final byte MAX_EMAIL_LENGTH = Byte.parseByte(getProperty("maxEmailLength"));

    public static void main(String[] args) throws NumberFormatException, InterruptedException {
        String driverLocation = getProperty("driverLocation");

        System.setProperty("webdriver.gecko.driver", driverLocation);
        WebDriver driver = new FirefoxDriver();
        setUpDriver(driver);

        List<Proxy> listIpPort = collectProxies(driver);
        List<String> credentials = new ArrayList<>();

        for (Proxy item : listIpPort) {
            FirefoxOptions options = new FirefoxOptions().setProfile(setUpProxy(item.getIp(), item.getPort()));
            WebDriver driverProxy = new FirefoxDriver(options);

            setUpDriver(driverProxy);
            String email = RandomStringUtils.randomAlphanumeric(MIN_EMAIL_LENGTH, MAX_EMAIL_LENGTH);

            try {
                fillOutSignUpForm(driverProxy, email);

                driverProxy.switchTo().activeElement();

                if (driverProxy.findElement(By.className("tos-scroll-button-icon")).isDisplayed()) {
                    do {
                        driverProxy.findElement(By.className("tos-scroll-button-icon")).click();
                        Thread.sleep(1000L);
                    } while (driverProxy.findElement(By.className("tos-scroll-button-icon")).isDisplayed());
                } else {
                    continue;
                }
                clickElement(driverProxy, "iagreebutton");
                if (driverProxy.findElements(By.cssSelector(".welcome>h1")).size() > 0) {
                    credentials.add(email + "@gmail.com," + getProperty("password"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                driverProxy.close();
            }
        }
        try {
            Files.write(Paths.get(getProperty("credentialsLocation")), credentials);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static FirefoxProfile setUpProxy(String ip, Integer port) {
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

    private static void chooseElementFromList(WebDriver driverProxy, By locatorOfList, String inputValue) {
        List<WebElement> elements = driverProxy.findElements(locatorOfList);
        for (WebElement element : elements) {
            if (element.getText().contains(inputValue)) {
                element.click();
            }
        }
    }

    private static void setUpDriver(WebDriver driver) {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    private static List<Proxy> collectProxies(WebDriver driver) {
        getPageURL(driver, "proxyListPage");

        setElementValueByCssSelector(driver, ".hx.ui-state-default>select");
        setElementValueByCssSelector(driver, ".hx.ui-state-default>select>[value='yes']");

        List<Proxy> listIpPort = new ArrayList<>();

        int numberOfIPAddresses = driver.findElements(By.cssSelector("#proxylisttable>tbody>tr")).size();

        for (int i = 1; i <= numberOfIPAddresses; i++) {
            listIpPort.add(new Proxy(driver.findElement(By.cssSelector("tbody>tr:nth-of-type(" + i + ")>td:nth-of-type(1)")).getText(),
                    Integer.parseInt(driver.findElement(By.cssSelector("tbody>tr:nth-of-type(" + i + ")>td:nth-of-type(2)")).getText())));
        }
        driver.close();
        return listIpPort;
    }

    private static void fillOutSignUpForm(WebDriver driver, String email) {
        getPageURL(driver, "signUpPage");
        setElementValueById(driver, "FirstName", "firstName");
        setElementValueById(driver, "LastName", "lastName");
        driver.findElement(By.id("GmailAddress")).sendKeys(email);
        setElementValueById(driver, "Passwd", "password");
        setElementValueById(driver, "PasswdAgain", "password");
        clickElement(driver, "BirthMonth");
        chooseElementFromList(driver, By.cssSelector("[id='BirthMonth'] [class='goog-menuitem-content']"),
                getProperty("birthMonth"));
        setElementValueById(driver, "BirthDay", "birthDay");
        setElementValueById(driver, "BirthYear", "birthYear");
        clickElement(driver, "Gender");
        chooseElementFromList(driver, By.cssSelector("[id='Gender'] [class='goog-menuitem-content']"),
                getProperty("gender"));
        clickElement(driver, "submitbutton");
    }

    private static void setElementValueById(WebDriver driver, String elementId, String elementValue) {
        driver.findElement(By.id(elementId)).sendKeys(getProperty(elementValue));
    }

    private static void setElementValueByCssSelector(WebDriver driver, String elementCssSelector) {
        driver.findElement(By.cssSelector(elementCssSelector)).click();
    }

    private static void clickElement(WebDriver driver, String elementId) {
        driver.findElement(By.id(elementId)).click();
    }

    private static void getPageURL(WebDriver driver, String pageURL) {
        driver.get(getProperty(pageURL));
    }

}
