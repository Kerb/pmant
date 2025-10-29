--liquibase formatted sql

--changeset pmant:0010-create-user-account-table
CREATE TABLE user_account
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    login         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

--changeset pmant:0020-insert-default-users-1
INSERT INTO user_account(id, login, password_hash)
VALUES (default, 'fluxoid@gmail.com', '$2a$10$8ckEjJi9dWiAt2VHHmPlBu1W.ow7KhBGJMZ7wZ9aRe8FeSOr6twQC');
COMMIT;

--changeset pmant:0020-insert-default-users-2
INSERT INTO user_account(id, login, password_hash)
VALUES (default, 'ctulhoo@gmail.com', '$2a$10$U4UI/XhahlPSybEUFE49uO7nhiv/OOkO/kE9/SKMuhNaVT7MgB2du');
COMMIT;

--changeset pmant:0020-users-meetings
CREATE TABLE user_meetings
(
    recording_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id      BIGINT       NOT NULL,
    title        VARCHAR(255) NOT NULL,
    status       VARCHAR(255) NOT NULL,
    file_name    VARCHAR(255) NOT NULL,
    file_path    VARCHAR(255) NOT NULL,
    duration     BIGINT       NOT NULL,
    speech       VARCHAR,
    upload_date  TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_account (id)
);

--changeset pmant:003-users-recordings-idx
CREATE INDEX idx_users_meetings_user_id ON user_meetings (user_id);
