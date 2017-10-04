package com.account;

import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

import static com.account.Driver.*;

public class ProxySupplier {

    public static List<Proxy> collectProxies() {

        AccountGoogle driver = new AccountGoogle();
        driver.goToUrl("proxyListPage");

        getElementBySelector(".hx.ui-state-default>select").click();
        getElementBySelector(".hx.ui-state-default>select>[value='yes']").click();

        List<Proxy> proxies = new ArrayList<>();

        int numberOfIPAddresses = getDriver().findElements(By.cssSelector("#proxylisttable>tbody>tr")).size();

        for (int i = 1; i <= numberOfIPAddresses; i++) {
            String port = getDriver()
                    .findElement(By.cssSelector("tbody>tr:nth-of-type(" + i + ")>td:nth-of-type(1)")).getText();
            String ip = getDriver()
                    .findElement(By.cssSelector("tbody>tr:nth-of-type(" + i + ")>td:nth-of-type(2)")).getText();
            Proxy proxy = new Proxy(port, Integer.parseInt(ip));

            proxies.add(proxy);
        }

        getDriver().close();

        return proxies;
    }
}
