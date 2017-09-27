package com.account;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AccountGenerator {

    public static void main(String[] args) throws InterruptedException, IOException {
        ResourceBundle properties = ResourceBundle.getBundle("config");
        String driverLocation = properties.getString("driverLocation");

        System.setProperty("webdriver.gecko.driver", driverLocation);
        WebDriver driver = new FirefoxDriver();
        setUpDriver(driver);

        Map<String, Integer> mapIpPort = collectProxies(driver);

        List<String> credentials = new ArrayList<>();

        for (Map.Entry<String, Integer> pairIpPort : mapIpPort.entrySet()) {
            WebDriver driverProxy = new FirefoxDriver(setUpProxy(pairIpPort.getKey(), pairIpPort.getValue()));
            setUpDriver(driverProxy);

            String email = RandomStringUtils.randomAlphanumeric(Integer.parseInt(properties.getString("minEmailLength")), Integer.parseInt(properties.getString("maxEmailLength")));

            fillOutSignUpForm (driverProxy, properties, email);

            driverProxy.switchTo().activeElement();

            if (driverProxy.findElement(By.className("tos-scroll-button-icon")).isDisplayed()) {
                do {
                    driverProxy.findElement(By.className("tos-scroll-button-icon")).click();
                    Thread.sleep(1000L);
                } while (driverProxy.findElement(By.className("tos-scroll-button-icon")).isDisplayed());
            } else {
                driverProxy.navigate().refresh();
                continue;
            }
            driverProxy.findElement(By.id("iagreebutton")).click();

            writeToFile(email, properties.getString("password"), credentials, properties);
            driverProxy.close();
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

    private static void writeToFile(String email, String password, List<String> credentials, ResourceBundle properties) throws IOException {
        credentials.add(email + "@gmail.com," + password);
        String credentialsLocation = properties.getString("credentialsLocation");
        Files.write(Paths.get(credentialsLocation), credentials);
    }

    private static void setUpDriver(WebDriver driver) {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    private static HashMap<String, Integer> collectProxies(WebDriver driver) {
        driver.get("https://free-proxy-list.net/");

        driver.findElement(By.cssSelector(".hx.ui-state-default>select")).click();
        driver.findElement(By.cssSelector(".hx.ui-state-default>select>[value='yes']")).click();
        driver.findElement(By.cssSelector("#proxylisttable>tfoot>tr>th:nth-of-type(5)>select")).click();
        driver.findElement(By.cssSelector("#proxylisttable>tfoot>tr>th:nth-of-type(5)>select>[value='anonymous']")).click();

        HashMap<String, Integer> mapIpPort = new HashMap<>();
        int numberOfIPAddresses = driver.findElements(By.cssSelector("#proxylisttable>tbody>tr")).size();

        for (int i = 1; i <= numberOfIPAddresses; i++) {
            mapIpPort.put(driver.findElement(By.cssSelector("tbody>tr:nth-of-type(" + i + ")>td:nth-of-type(1)")).getText(),
                    Integer.parseInt(driver.findElement(By.cssSelector("tbody>tr:nth-of-type(" + i + ")>td:nth-of-type(2)")).getText()));
        }
        driver.close();

        return mapIpPort;
    }

    private static void fillOutSignUpForm (WebDriver driverProxy, ResourceBundle properties, String email) {
        driverProxy.get(properties.getString("signUpPage"));
        driverProxy.findElement(By.id("FirstName")).sendKeys(properties.getString("firstName"));
        driverProxy.findElement(By.id("LastName")).sendKeys(properties.getString("lastName"));

        driverProxy.findElement(By.id("GmailAddress")).sendKeys(email);
        driverProxy.findElement(By.id("Passwd")).sendKeys(properties.getString("password"));
        driverProxy.findElement(By.id("PasswdAgain")).sendKeys(properties.getString("password"));
        driverProxy.findElement(By.id("BirthMonth")).click();

        chooseElementFromList(driverProxy, By.cssSelector("[id='BirthMonth'] [class='goog-menuitem-content']"), properties.getString("birthMonth"));

        driverProxy.findElement(By.id("BirthDay")).sendKeys(properties.getString("birthDay"));
        driverProxy.findElement(By.id("BirthYear")).sendKeys(properties.getString("birthYear"));
        driverProxy.findElement(By.id("Gender")).click();

        chooseElementFromList(driverProxy, By.cssSelector("[id='Gender'] [class='goog-menuitem-content']"), properties.getString("gender"));

        driverProxy.findElement(By.id("submitbutton")).click();
    }

}
