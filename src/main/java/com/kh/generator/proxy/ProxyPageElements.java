package com.kh.generator.proxy;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static com.kh.generator.common.Property.*;

public class ProxyPageElements {

    private static final String URL = getProperty("proxyListPage");

    public ProxyPageElements(WebDriver driver) {
        driver.get(URL);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = ".hx.ui-state-default>select")
    private WebElement httpsDropdown;

    @FindBy(css = ".hx.ui-state-default>select>[value='yes']")
    private WebElement httpsYesValue;

    @FindBy(css = "tbody>tr")
    private List<WebElement> tableRows;

    private By ipColumn = By.cssSelector("td:nth-of-type(1)");

    private By portColumn = By.cssSelector("td:nth-of-type(2)");

    public WebElement getHttpsDropdown() {
        return httpsDropdown;
    }

    public WebElement getHttpsYesValue() {
        return httpsYesValue;
    }

    public List<WebElement> getTableRows() {
        return tableRows;
    }

    public By getIpColumn() {
        return ipColumn;
    }

    public By getPortColumn() {
        return portColumn;
    }

}
