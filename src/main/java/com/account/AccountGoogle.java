package com.account;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

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

        AccountGoogle driver = new AccountGoogle();
        Credentials credentials = new Credentials();

        for (Proxy proxy : proxies) {
            FirefoxOptions options = new FirefoxOptions().setProfile(setUpProxyProfile(proxy.getIp(), proxy.getPort()));
            new Driver(options);
            setUpDriver(getDriver());

            String email = RandomStringUtils.randomAlphanumeric(MIN_EMAIL_LENGTH, MAX_EMAIL_LENGTH);

            try {
                driver.fillOutSignUpForm(email);
                getDriver().switchTo().activeElement();

                (new WebDriverWait(getDriver(), 5))
                        .until(ExpectedConditions.elementToBeClickable(By.className("tos-scroll-button-icon")));

                do {
                    getDriver().findElement(By.className("tos-scroll-button-icon")).click();
                    Thread.sleep(1000L);
                } while (getDriver().findElement(By.className("tos-scroll-button-icon")).isDisplayed());
                getElementById("iagreebutton").click();
                if (getDriver().findElements(By.cssSelector(".welcome>h1")).size() > 0) {
                    credentials.addRecord(email, getProperty("password"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                getDriver().close();
            }
        }
        credentials.writeToFile();
    }

    @Override
    public void goToUrl(String url) {
        getDriver().get(getProperty(url));
    }

    @Override
    public void fillOutSignUpForm(String email) throws InterruptedException {
        goToUrl("signUpPage");
        getElementById("FirstName").sendKeys(getProperty("firstName"));
        getElementById("LastName").sendKeys(getProperty("lastName"));
        getElementById("GmailAddress").sendKeys(email);
        getElementById("Passwd").sendKeys(getProperty("password"));
        getElementById("PasswdAgain").sendKeys(getProperty("password"));
        getElementById("BirthMonth").click();
        selectElementByValue(By.cssSelector("[id='BirthMonth'] [class='goog-menuitem-content']"),
                getProperty("birthMonth"));
        getElementById("BirthDay").sendKeys(getProperty("birthDay"));
        getElementById("BirthYear").sendKeys(getProperty("birthYear"));
        getElementById("Gender").click();
        selectElementByValue(By.cssSelector("[id='Gender'] [class='goog-menuitem-content']"),
                getProperty("gender"));
        Thread.sleep(1000L);
        getElementById("submitbutton").click();
    }
}
