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

public class AccountGenerator {

    public static void main(String[] args) throws IOException {
        ResourceBundle properties = ResourceBundle.getBundle("config");
        String driverLocation = properties.getString("driverLocation");

        System.setProperty("webdriver.gecko.driver", driverLocation);
        WebDriver driver = new FirefoxDriver();
        setUpDriver(driver);

        List<Proxy> listIpPort = collectProxies(driver, properties);
        List<String> credentials = new ArrayList<>();

        for (Proxy item : listIpPort) {
            final FirefoxOptions options = new FirefoxOptions().setProfile(setUpProxy(item.getIp(), item.getPort()));
            final WebDriver driverProxy = new FirefoxDriver(options);

            setUpDriver(driverProxy);
            String email = RandomStringUtils.randomAlphanumeric(Integer.parseInt(properties.getString("minEmailLength")),
                    Integer.parseInt(properties.getString("maxEmailLength")));

            try {
                fillOutSignUpForm(driverProxy, properties, email);

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
                    credentials.add(email + "@gmail.com," + properties.getString("password"));
                }
            } catch (NumberFormatException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                Files.write(Paths.get(properties.getString("credentialsLocation")), credentials);
                driverProxy.close();
            }
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

    private static List<Proxy> collectProxies(WebDriver driver, ResourceBundle properties) {
        getPageURL(driver, properties, "proxyListPage");

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

    private static void fillOutSignUpForm(WebDriver driver, ResourceBundle properties, String email) {
        getPageURL(driver, properties, "signUpPage");
        setElementValueById(driver, properties, "FirstName", "firstName");
        setElementValueById(driver, properties, "LastName", "lastName");
        driver.findElement(By.id("GmailAddress")).sendKeys(email);
        setElementValueById(driver, properties, "Passwd", "password");
        setElementValueById(driver, properties, "PasswdAgain", "password");
        clickElement(driver, "BirthMonth");
        chooseElementFromList(driver, By.cssSelector("[id='BirthMonth'] [class='goog-menuitem-content']"),
                properties.getString("birthMonth"));
        setElementValueById(driver, properties, "BirthDay", "birthDay");
        setElementValueById(driver, properties, "BirthYear", "birthYear");
        clickElement(driver, "Gender");
        chooseElementFromList(driver, By.cssSelector("[id='Gender'] [class='goog-menuitem-content']"),
                properties.getString("gender"));
        clickElement(driver, "submitbutton");
    }

    private static void setElementValueById(WebDriver driver, ResourceBundle properties, String elementId, String elementValue) {
        driver.findElement(By.id(elementId)).sendKeys(properties.getString(elementValue));
    }

    private static void setElementValueByCssSelector(WebDriver driver, String elementCssSelector) {
        driver.findElement(By.cssSelector(elementCssSelector)).click();
    }

    private static void clickElement(WebDriver driver, String elementId) {
        driver.findElement(By.id(elementId)).click();
    }

    private static void getPageURL(WebDriver driver, ResourceBundle properties, String pageURL) {
        driver.get(properties.getString(pageURL));
    }

}
