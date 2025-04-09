package com.mail.sender.util;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@UtilityClass
public class MailProperties {
    private final Properties properties;
    private static final String MAIL_SUBJECT_KEY = "mail.subject";

    static {
        properties = loadProperties();
    }

    public static String getMailSubject() {
        return properties.getProperty(MAIL_SUBJECT_KEY);
    }

    private static Properties loadProperties() {
        try(InputStream inputStream = MailProperties.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
