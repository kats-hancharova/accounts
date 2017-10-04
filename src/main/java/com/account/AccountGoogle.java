package com.account;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.account.Property.*;
import static com.account.ProxySupplier.*;
import static com.account.Driver.*;

public class AccountGoogle implements AccountGenerator {

    private static final byte MIN_EMAIL_LENGTH = Byte.parseByte(getProperty("minEmailLength"));
    private static final byte MAX_EMAIL_LENGTH = Byte.parseByte(getProperty("maxEmailLength"));

    public static void main(String[] args) throws NumberFormatException, InterruptedException {
        new Driver();
        setUpDriver(getDriver());

        List<Proxy> proxies = collectProxies();
        List<String> credentials = new ArrayList<>();

        AccountGoogle googleAccount = new AccountGoogle();

        for (Proxy proxy : proxies) {
            FirefoxOptions options = new FirefoxOptions().setProfile(setUpProxyProfile(proxy.getIp(), proxy.getPort()));
            new Driver(options);
            setUpDriver(getDriver());

            String email = RandomStringUtils.randomAlphanumeric(MIN_EMAIL_LENGTH, MAX_EMAIL_LENGTH);

            try {
                googleAccount.fillOutSignUpForm(email);

                getDriver().switchTo().activeElement();

                (new WebDriverWait(getDriver(), 5))
                        .until(ExpectedConditions.elementToBeClickable(By.className("tos-scroll-button-icon")));

                do {
                    getDriver().findElement(By.className("tos-scroll-button-icon")).click();
                    Thread.sleep(1000L);
                } while (getDriver().findElement(By.className("tos-scroll-button-icon")).isDisplayed());
                getElementById("iagreebutton").click();
                if (getDriver().findElements(By.cssSelector(".welcome>h1")).size() > 0) {
                    credentials.add(email + "@gmail.com," + getProperty("password"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                getDriver().close();
            }
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date();
        try {
            Files.write(Paths.get(getProperty("credentialsLocation")
                    + getProperty("credentialsFileName")
                    + dateFormat.format(date)
                    + getProperty("credentialsFileExt")), credentials);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static FirefoxProfile setUpProxyProfile(String ip, Integer port) {
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

    public static void chooseElementFromList(By locatorOfList, String inputValue) {
        List<WebElement> elements = getDriver().findElements(locatorOfList);
        for (WebElement element : elements) {
            if (element.getText().contains(inputValue)) {
                element.click();
            }
        }
    }

    @Override
    public void fillOutSignUpForm(String email) throws InterruptedException {
        goToUrl("signUpPage");
        getElementById("FirstName").sendKeys(getProperty("firstName"));
        getElementById("LastName").sendKeys(getProperty("lastName"));
        getDriver().findElement(By.id("GmailAddress")).sendKeys(email);
        getElementById("Passwd").sendKeys(getProperty("password"));
        getElementById("PasswdAgain").sendKeys(getProperty("password"));
        getElementById("BirthMonth").click();
        chooseElementFromList(By.cssSelector("[id='BirthMonth'] [class='goog-menuitem-content']"),
                getProperty("birthMonth"));
        getElementById("BirthDay").sendKeys(getProperty("birthDay"));
        getElementById("BirthYear").sendKeys(getProperty("birthYear"));
        getElementById("Gender").click();
        chooseElementFromList(By.cssSelector("[id='Gender'] [class='goog-menuitem-content']"),
                getProperty("gender"));
        Thread.sleep(1000L);
        getElementById("submitbutton").click();
    }

    public static WebElement getElementById(String id) {
        return getDriver().findElement(By.id(id));
    }

    public static WebElement getElementBySelector(String selector) {
        return getDriver().findElement(By.cssSelector(selector));
    }

    public static void goToUrl(String pageURL) {
        getDriver().get(getProperty(pageURL));
    }

}
