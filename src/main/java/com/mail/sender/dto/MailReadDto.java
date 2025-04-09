package com.mail.sender.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class MailReadDto {
    Long id;
    String email;
    String username;
    String status;
    String sentBy;
    LocalDateTime sentAt;
}
