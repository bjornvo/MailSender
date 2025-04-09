package com.mail.sender.service;

import com.mail.sender.dto.MailReadDto;
import com.mail.sender.entity.MailEntity;
import com.mail.sender.mapper.MailMapper;
import com.mail.sender.parser.Parser;
import com.mail.sender.repository.MailRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.mail.sender.entity.MailEntity.Status.DELIVERED;
import static com.mail.sender.entity.MailEntity.Status.FAILED;
import static com.mail.sender.util.HTMLCodeExtractor.getContent;
import static com.mail.sender.util.MailProperties.getMailSubject;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmailService {
    private static final String PATTERN = "{username}";

    private final JavaMailSender javaMailSender;
    private final MailRepository mailRepository;
    private final MailMapper mailMapper;
    private final Parser xlsxParser;

    @Value("${spring.mail.username}")
    private String sendersMail;

    public void sendEmail(MultipartFile table, MultipartFile htmlFile) {
        try (InputStream tableInputStream = table.getInputStream();
             InputStream htmlFileInputStream = htmlFile.getInputStream()) {
            Map<String, String> emailAndUsername = prepareEmailData(tableInputStream);
            String htmlFileContent = getContent(htmlFileInputStream);
            processEmails(emailAndUsername, htmlFileContent);
        } catch (IOException e) {
            log.error("Troubles with data: {}", e.getMessage());
        }
    }

    private void processEmails(Map<String, String> emailAndUsername, String htmlFileContent) {
        for (Map.Entry<String, String> entry : emailAndUsername.entrySet()) {
            String email = entry.getKey();
            String username = entry.getValue();
            String content = getContentWithReplacedUsername(htmlFileContent, username);
            log.info("Processing email: {}, with username: {}", email, username);
            sendEmail(email, content, username);
        }
    }

    private void sendEmail(String email, String content, String username) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            message.setHeader("Content-Type", "text/html; charset=UTF-8");
            message.setHeader("Content-Transfer-Encoding", "8bit");

            helper.setSubject(getMailSubject());
            helper.setTo(email);
            helper.setText(content, true);
            javaMailSender.send(message);
            log.info("Email sent successfully to: {}", email);
            saveMail(email, username, DELIVERED);
        } catch (Exception e) {
            log.error("Failed to send email to {}, from {}: {}", email, sendersMail, e.getMessage(), e);
            saveMail(email, username, FAILED);
        }
    }

    private Map<String, String> prepareEmailData(InputStream tableInputStream) {
        return xlsxParser.getEmailsAndUsernamesMap(tableInputStream);
    }

    public List<MailReadDto> findAll(LocalDateTime from, LocalDateTime to) {
        return mailRepository.findAll(from, to).stream()
                .map(mailMapper::toReadDto)
                .toList();
    }

    private void saveMail(String email, String username, MailEntity.Status status) {
        MailEntity entity = MailEntity.builder()
                .email(email)
                .username(username)
                .status(status)
                .sentBy(sendersMail)
                .sentAt(LocalDateTime.now())
                .build();
        mailRepository.save(entity);
    }

    private static String getContentWithReplacedUsername(String htmlFileContent, String username) {
        return htmlFileContent.replace(PATTERN, username);
    }
}