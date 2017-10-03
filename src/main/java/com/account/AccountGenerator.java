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
import java.util.*;

import static com.account.Property.*;
import static com.account.Driver.*;

public class AccountGenerator {

    private static final byte MIN_EMAIL_LENGTH = Byte.parseByte(getProperty("minEmailLength"));
    private static final byte MAX_EMAIL_LENGTH = Byte.parseByte(getProperty("maxEmailLength"));

    public static void main(String[] args) throws NumberFormatException, InterruptedException {
        new Driver();
        setUpDriver(getDriver());

        List<Proxy> listIpPort = collectProxies();
        List<String> credentials = new ArrayList<>();

        for (Proxy item : listIpPort) {
            FirefoxOptions options = new FirefoxOptions().setProfile(setUpProxyProfile(item.getIp(), item.getPort()));
            new Driver(options);
            setUpDriver(getDriver());

            String email = RandomStringUtils.randomAlphanumeric(MIN_EMAIL_LENGTH, MAX_EMAIL_LENGTH);

            try {
                fillOutSignUpForm(email);

                getDriver().switchTo().activeElement();

                (new WebDriverWait(getDriver(), 5))
                        .until(ExpectedConditions.elementToBeClickable(By.className("tos-scroll-button-icon")));

                do {
                    getDriver().findElement(By.className("tos-scroll-button-icon")).click();
                    Thread.sleep(1000L);
                } while (getDriver().findElement(By.className("tos-scroll-button-icon")).isDisplayed());
                clickElement("iagreebutton");
                if (getDriver().findElements(By.cssSelector(".welcome>h1")).size() > 0) {
                    credentials.add(email + "@gmail.com," + getProperty("password"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                getDriver().close();
            }
        }
        try {
            Files.write(Paths.get(getProperty("credentialsLocation")), credentials);
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

    private static void chooseElementFromList(By locatorOfList, String inputValue) {
        List<WebElement> elements = getDriver().findElements(locatorOfList);
        for (WebElement element : elements) {
            if (element.getText().contains(inputValue)) {
                element.click();
            }
        }
    }

    private static List<Proxy> collectProxies() {
        getPageURL("proxyListPage");

        setElementValueByCssSelector(".hx.ui-state-default>select");
        setElementValueByCssSelector(".hx.ui-state-default>select>[value='yes']");

        List<Proxy> listIpPort = new ArrayList<>();

        int numberOfIPAddresses = getDriver().findElements(By.cssSelector("#proxylisttable>tbody>tr")).size();

        for (int i = 1; i <= numberOfIPAddresses; i++) {
            listIpPort.add(new Proxy(getDriver().findElement
                    (By.cssSelector("tbody>tr:nth-of-type(" + i + ")>td:nth-of-type(1)")).getText(),
                    Integer.parseInt(getDriver().findElement
                            (By.cssSelector("tbody>tr:nth-of-type(" + i + ")>td:nth-of-type(2)")).getText())));
        }
        getDriver().close();
        return listIpPort;
    }

    private static void fillOutSignUpForm(String email) throws InterruptedException {
        getPageURL("signUpPage");
        setElementValueById("FirstName", "firstName");
        setElementValueById("LastName", "lastName");
        getDriver().findElement(By.id("GmailAddress")).sendKeys(email);
        setElementValueById("Passwd", "password");
        setElementValueById("PasswdAgain", "password");
        clickElement("BirthMonth");
        chooseElementFromList(By.cssSelector("[id='BirthMonth'] [class='goog-menuitem-content']"),
                getProperty("birthMonth"));
        setElementValueById("BirthDay", "birthDay");
        setElementValueById("BirthYear", "birthYear");
        clickElement("Gender");
        chooseElementFromList(By.cssSelector("[id='Gender'] [class='goog-menuitem-content']"),
                getProperty("gender"));
        Thread.sleep(1000L);
        clickElement("submitbutton");
    }

    private static void setElementValueById(String elementId, String elementValue) {
        getDriver().findElement(By.id(elementId)).sendKeys(getProperty(elementValue));
    }

    private static void setElementValueByCssSelector(String elementCssSelector) {
        getDriver().findElement(By.cssSelector(elementCssSelector)).click();
    }

    private static void clickElement(String elementId) {
        getDriver().findElement(By.id(elementId)).click();
    }

    private static void getPageURL(String pageURL) {
        getDriver().get(getProperty(pageURL));
    }

}
