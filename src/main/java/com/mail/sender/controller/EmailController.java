package com.mail.sender.controller;

import com.mail.sender.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/mail-sender")
@RequiredArgsConstructor
@Slf4j
public class EmailController {
    private final EmailService emailService;

    @GetMapping
    public String getEmailSenderPage() {
        return "sender";
    }

    @GetMapping("/reports")
    public String findAll(@RequestParam("from") LocalDate from,
                          @RequestParam("to") LocalDate to,
                          Model model) {
        LocalDateTime fromDateTime = LocalDateTime.of(from.getYear(), from.getMonth(), from.getDayOfMonth(), 0, 0, 0);
        LocalDateTime toDateTime = LocalDateTime.of(to.getYear(), to.getMonth(), to.getDayOfMonth(), 23, 59, 59);
        model.addAttribute("mails", emailService.findAll(fromDateTime, toDateTime));
        return "report";
    }

    @PostMapping
    public String uploadAndProcess(@RequestParam("table") MultipartFile table,
                                   @RequestParam("htmlFile") MultipartFile htmlFile) {
        emailService.sendEmail(table, htmlFile);
        return "redirect:/mail-sender";
    }
}
