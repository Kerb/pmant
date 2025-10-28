--liquibase formatted sql

--changeset pmant:001-create-user-account-table
CREATE TABLE user_account
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    login         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);


--changeset pmant:002-users-meetings
CREATE TABLE user_meetings
(
    recording_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id      BIGINT       NOT NULL,
    title        VARCHAR(255) NOT NULL,
    status        VARCHAR(255) NOT NULL,
    file_name    VARCHAR(255) NOT NULL,
    file_path    VARCHAR(255) NOT NULL,
    duration     BIGINT       NOT NULL,
    upload_date  TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_account (id)
);

--changeset pmant:003-users-recordings-idx
CREATE INDEX idx_users_meetings_user_id ON user_meetings (user_id);
