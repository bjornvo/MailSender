package com.mail.sender.mapper;

import com.mail.sender.dto.MailReadDto;
import com.mail.sender.entity.MailEntity;
import org.springframework.stereotype.Component;

@Component
public class MailMapper {
    public MailReadDto toReadDto(MailEntity entity) {
        return MailReadDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .username(entity.getUsername())
                .status(entity.getStatus().name())
                .sentBy(entity.getSentBy())
                .sentAt(entity.getSentAt())
                .build();
    }
}
