package com.mail.sender.parser;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class XlsxParser implements Parser {
    public Map<String, String> getEmailsAndUsernamesMap(InputStream file) {
        try (Workbook workbook = new XSSFWorkbook(file)) {
            Map<String, String> emailsAndFullNames = new HashMap<>();
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getCell(3).getStringCellValue().equals("Email")) {
                    continue;
                }
                String email = row.getCell(3).getStringCellValue();
                String username = row.getCell(2).getStringCellValue();
                emailsAndFullNames.put(email, username);
                log.info("Data was successfully extracted from table. Email: {}, username: {}", email, username);
            }
            return emailsAndFullNames;
        } catch (IOException e) {
            log.error("Troubles with .xlsx file. Check for valid data: {}", e.getMessage());
            return new HashMap<>();
        }
    }
}
