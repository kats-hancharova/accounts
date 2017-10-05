package com.account;

import java.util.ArrayList;
import java.util.List;

public class ProxySupplier {

    public List<Proxy> collectProxies() {

        Driver driver = new Driver();

        driver.goToUrl("proxyListPage");

        driver.getElementBySelector(".hx.ui-state-default>select").click();
        driver.getElementBySelector(".hx.ui-state-default>select>[value='yes']").click();

        List<Proxy> proxies = new ArrayList<>();

        int numberOfIPAddresses = driver.getElementsBySelector("#proxylisttable>tbody>tr").size();

        for (int i = 1; i <= numberOfIPAddresses; i++) {
            String port = driver
                    .getElementBySelector("tbody>tr:nth-of-type(" + i + ")>td:nth-of-type(1)").getText();
            String ip = driver
                    .getElementBySelector("tbody>tr:nth-of-type(" + i + ")>td:nth-of-type(2)").getText();
            Proxy proxy = new Proxy(port, Integer.parseInt(ip));

            proxies.add(proxy);
        }

        driver.close();

        return proxies;
    }
}
