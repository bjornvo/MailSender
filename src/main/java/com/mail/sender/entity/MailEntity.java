package com.mail.sender.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailEntity {
    private Long id;
    private String email;
    private String username;
    private Status status;
    private String sentBy;
    private LocalDateTime sentAt;

    public enum Status {
        DELIVERED, FAILED
    }
}
