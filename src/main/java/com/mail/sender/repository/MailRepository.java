package com.mail.sender.repository;

import com.mail.sender.entity.MailEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class MailRepository {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private static final String SAVE_SQL = """
            INSERT INTO mails(email, username, status, sent_by, sent_at)
            VALUES (:email, :username, :status, :sentBy,  :sentAt);
            """;

    private static final String FIND_ALL_BETWEEN_SQL = """
            SELECT
                id,
                email,
                username,
                status,
                sent_by,
                sent_at
            FROM mails
            WHERE sent_at BETWEEN :from AND :to;
            """;

    public Long save(MailEntity entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "email", entity.getEmail(),
                "username", entity.getUsername(),
                "status", entity.getStatus().name(),
                "sentBy", entity.getSentBy(),
                "sentAt", Timestamp.valueOf(entity.getSentAt())
        ));
        namedJdbcTemplate.update(SAVE_SQL, params, keyHolder, new String[]{"id"});
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<MailEntity> findAll(LocalDateTime from, LocalDateTime to) {
        Map<String, Timestamp> params = Map.of("from", Timestamp.valueOf(from), "to", Timestamp.valueOf(to));
        return namedJdbcTemplate.query(FIND_ALL_BETWEEN_SQL, params, getMailRowMapper());
    }

    private static RowMapper<MailEntity> getMailRowMapper() {
        return (rs, rowNum) -> new MailEntity(
                rs.getObject("id", Long.class),
                rs.getObject("email", String.class),
                rs.getObject("username", String.class),
                MailEntity.Status.valueOf(rs.getObject("status", String.class)),
                rs.getObject("sent_by", String.class),
                rs.getTimestamp("sent_at").toLocalDateTime()
        );
    }
}
