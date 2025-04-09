package com.mail.sender.parser;

import java.io.InputStream;
import java.util.Map;

public interface Parser {
    Map<String, String> getEmailsAndUsernamesMap(InputStream file);
}
