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

public class CredentialsStore {

    private List<String> credentialsList = new ArrayList<>();

    public void writeToFile() {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date();

        try {
            StringBuilder credentialsRecord = new StringBuilder();
            Files.write(Paths.get(credentialsRecord
                    .append(getProperty("credentialsLocation"))
                    .append(getProperty("credentialsFileName"))
                    .append(dateFormat.format(date))
                    .append(getProperty("credentialsFileExt")).toString()), credentialsList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToList(Credentials credentials) {
        credentialsList.add(credentials.getEmail() + "@gmail.com," + credentials.getPassword());
    }
}
