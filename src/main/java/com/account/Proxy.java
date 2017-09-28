package com.account;

public class Proxy {
    String ip;
    Integer port;

    public Proxy (String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }
}
