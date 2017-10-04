package com.account;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.account.Property.getProperty;

public class Credentials {

    private List<String> credentials = new ArrayList<>();

    public void writeToFile() {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date();

        try {
            Files.write(Paths.get(getProperty("credentialsLocation")
                    + getProperty("credentialsFileName")
                    + dateFormat.format(date)
                    + getProperty("credentialsFileExt")), credentials);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addRecord(String email, String password) {
        credentials.add(email + "@gmail.com," + password);
    }
}
