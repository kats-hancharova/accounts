package com.account;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.util.List;

import static com.account.Property.getProperty;

public class GoogleAccountGenerator implements AccountGenerator {

    private static final byte MIN_EMAIL_LENGTH = Byte.parseByte(getProperty("minEmailLength"));
    private static final byte MAX_EMAIL_LENGTH = Byte.parseByte(getProperty("maxEmailLength"));

    private CredentialsStore credentialsStore = new CredentialsStore();

    public void execute() {

        ProxySupplier supplier = new ProxySupplier();
        List<Proxy> proxies = supplier.collectProxies();

        for (Proxy proxy : proxies) {
            FirefoxOptions options = new FirefoxOptions().setProfile(setUpProxyProfile(proxy.getIp(), proxy.getPort()));
            Driver driver = new Driver(options);

            Credentials credentials = new Credentials(generateEmail());

            try {
                fillOutSignUpForm(driver, credentials.getEmail());

                driver.toActiveElement();

                driver.waitForElement("tos-scroll-button-icon");

                do {
                    driver.getElementByClass("tos-scroll-button-icon").click();
                    Thread.sleep(1000L);
                } while (driver.getElementByClass("tos-scroll-button-icon").isDisplayed());
                driver.getElementById("iagreebutton").click();
                if (isRegistrationSuccessful(driver)) {
                    credentialsStore.addToList(credentials);
                }
            } catch (NumberFormatException | InterruptedException | WebDriverException e) {
                e.printStackTrace();
            } finally {
                driver.close();
            }
        }
        credentialsStore.writeToFile();
    }

    @Override
    public void fillOutSignUpForm(Driver driver, String email) throws InterruptedException {
        driver.goToUrl("signUpPage");
        driver.getElementById("FirstName").sendKeys(getProperty("firstName"));
        driver.getElementById("LastName").sendKeys(getProperty("lastName"));
        driver.getElementById("GmailAddress").sendKeys(email);
        driver.getElementById("Passwd").sendKeys(getProperty("password"));
        driver.getElementById("PasswdAgain").sendKeys(getProperty("password"));
        driver.getElementById("BirthMonth").click();
        driver.selectElementByValue(By.cssSelector("[id='BirthMonth'] [class='goog-menuitem-content']"),
                getProperty("birthMonth"));
        driver.getElementById("BirthDay").sendKeys(getProperty("birthDay"));
        driver.getElementById("BirthYear").sendKeys(getProperty("birthYear"));
        driver.getElementById("Gender").click();
        driver.selectElementByValue(By.cssSelector("[id='Gender'] [class='goog-menuitem-content']"),
                getProperty("gender"));
        Thread.sleep(1000L);
        driver.getElementById("submitbutton").click();
    }

    public String generateEmail() {
        return RandomStringUtils.randomAlphanumeric(MIN_EMAIL_LENGTH, MAX_EMAIL_LENGTH);
    }

    public boolean isRegistrationSuccessful(Driver driver) {
        return driver.getElementsBySelector(".welcome>h1").size() > 0 ? true : false;
    }

    public FirefoxProfile setUpProxyProfile(String ip, Integer port) {
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
