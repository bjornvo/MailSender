--liquibase formatted sql

--changeset sender:1
CREATE TABLE IF NOT EXISTS mails
(
    id       BIGSERIAL PRIMARY KEY,
    email    VARCHAR(128),
    username VARCHAR(128),
    status   VARCHAR(16),
    sent_by VARCHAR(128),
    sent_at  TIMESTAMP
);