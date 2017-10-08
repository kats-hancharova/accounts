package com.kh.generator.google;

import com.kh.generator.common.UserDetails;
import com.kh.generator.common.CredentialsStore;
import com.kh.generator.proxy.Proxy;
import com.kh.generator.proxy.ProxySupplier;
import com.kh.generator.interfaces.AccountGenerator;
import com.kh.generator.webdriver.Driver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.util.List;

public class GoogleAccountGenerator implements AccountGenerator {

    private CredentialsStore credentialsStore = new CredentialsStore();

    public void execute() {

        ProxySupplier supplier = new ProxySupplier();
        List<Proxy> proxies = supplier.collectProxies();

        for (Proxy proxy : proxies) {
            FirefoxOptions options = new FirefoxOptions()
                    .setProfile(setUpProxyProfile(proxy.getIp(), proxy.getPort()));
            Driver driver = new Driver(options);

            UserDetails userDetails = new UserDetails();

            try {
                SignUpPageElements pageElements = new SignUpPageElements(driver.getDriver());

                fillOutSignUpForm(driver, pageElements, userDetails);

                driver.getDriver().switchTo().activeElement();
                driver.waitForElement(pageElements.getScrollDown());

                do {
                    pageElements.getScrollDown().click();
                    Thread.sleep(1000L);
                } while (pageElements.getScrollDown().isDisplayed());

                pageElements.getAgreeButton().click();

                if (isRegistrationSuccessful(driver)) {
                    credentialsStore.addToList(userDetails);
                }
            } catch (NumberFormatException | InterruptedException | WebDriverException e) {
                e.printStackTrace();
            } finally {
                driver.getDriver().close();
            }
        }

        credentialsStore.writeToFile();
    }

    @Override
    public void fillOutSignUpForm(Driver driver, SignUpPageElements pageElements, UserDetails userDetails)
            throws InterruptedException {

        pageElements.getFirstName().sendKeys(userDetails.getFirstName());
        pageElements.getLastName().sendKeys(userDetails.getLastName());
        pageElements.getEmail().sendKeys(userDetails.getEmail());
        pageElements.getPassword().sendKeys(userDetails.getPassword());
        pageElements.getPasswordConfirm().sendKeys(userDetails.getPassword());
        pageElements.getBirthMonthDropdown().click();

        driver.getElementFromList(pageElements.getBirthMonth(),
                userDetails.getBirthMonth());

        pageElements.getBirthDay().sendKeys(userDetails.getBirthDay());
        pageElements.getBirthYear().sendKeys(userDetails.getBirthYear());
        pageElements.getGenderDropdown().click();

        driver.getElementFromList(pageElements.getGender(),
                userDetails.getGender());

        Thread.sleep(1000L);
        pageElements.getSubmitButton().click();
    }

    public boolean isRegistrationSuccessful(Driver driver) {
        WelcomePageElements pageElements = new WelcomePageElements(driver.getDriver());
        return (pageElements.getWelcomeText().size() > 0) ? true : false;
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
