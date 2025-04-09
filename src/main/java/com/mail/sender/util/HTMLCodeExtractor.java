package com.mail.sender.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.util.stream.Collectors.*;

@Slf4j
@UtilityClass
public class HTMLCodeExtractor {
    public static String getContent(InputStream htmlFileInputStream) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(htmlFileInputStream))) {
            return reader.lines().collect(joining());
        } catch (IOException e) {
            log.error("Failed to process the template file. {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

