package com.mail.sender.parser;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CsvParser implements Parser {
    public Map<String, String> getEmailsAndUsernamesMap(InputStream file) {
        Map<String, String> emailsAndFullNames = new HashMap<>();
        try(CSVReader reader = new CSVReader(new InputStreamReader(file))) {
            String[] line;
            reader.readNext(); // skip headers
            while ((line = reader.readNext()) != null) {
                emailsAndFullNames.put(line[1], line[0]);
            }
            return emailsAndFullNames;
        } catch (IOException | CsvValidationException e) {
            log.error("Exception occurred: {}, in class: {}", e.getMessage(), CsvParser.class);
            throw new RuntimeException(e);
        }
    }
}
