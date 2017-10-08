package com.kh.generator.proxy;

import com.kh.generator.webdriver.Driver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class ProxySupplier {

    public List<Proxy> collectProxies() {

        Driver driver = new Driver();

        ProxyPageElements pageElements = new ProxyPageElements(driver.getDriver());

        pageElements.getHttpsDropdown().click();
        pageElements.getHttpsYesValue().click();

        List<Proxy> proxies = new ArrayList<>();

        List<WebElement> tableRows = pageElements.getTableRows();

        for (WebElement tableRow : tableRows) {
            String ip = tableRow.findElement(pageElements.getIpColumn()).getText();
            String port = tableRow.findElement(pageElements.getPortColumn()).getText();

            Proxy proxy = new Proxy(ip, Integer.parseInt(port));
            proxies.add(proxy);
        }

        driver.getDriver().close();

        return proxies;
    }

}
