CREATE TABLE channels
(
    id          uuid PRIMARY KEY,
    created_at  timestamptz NOT NULL,
    updated_at  timestamptz,
    name        varchar(100),
    description varchar(500),
    type        varchar(10) NOT NULL,

    CONSTRAINT channel_type
        CHECK ( type in ('PUBLIC', 'PRIVATE') )
);

CREATE TABLE binary_contents
(
    id           uuid PRIMARY KEY,
    created_at   timestamptz  NOT NULL,
    file_name    varchar(255) NOT NULL,
    size         bigint       NOT NULL,
    content_type varchar(100) NOT NULL,
    bytes        bytea        NOT NULL
);



CREATE TABLE users
(
    id         uuid PRIMARY KEY,
    created_at timestamptz         NOT NULL,
    updated_at timestamptz,
    username   varchar(50) UNIQUE  NOT NULL,
    email      varchar(100) UNIQUE NOT NULL,
    password   varchar(60)         NOT NULL,
    profile_id uuid UNIQUE,
    FOREIGN KEY (profile_id)
        REFERENCES binary_contents
        ON DELETE SET NULL
);

CREATE TABLE user_statuses
(
    id             uuid PRIMARY KEY,
    created_at     timestamptz NOT NULL,
    updated_at     timestamptz,
    last_active_at timestamptz NOT NULL,
    user_id        uuid UNIQUE NOT NULL,
    FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
);

CREATE TABLE read_statuses
(
    id           uuid PRIMARY KEY,
    created_at   timestamptz NOT NULL,
    updated_at   timestamptz,
    last_read_at timestamptz NOT NULL,
    user_id      uuid        NOT NULL,
    channel_id   uuid        NOT NULL,
    FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE,
    FOREIGN KEY (channel_id)
        REFERENCES channels (id)
        ON DELETE CASCADE,
    UNIQUE (user_id, channel_id)
);

CREATE TABLE messages
(
    id         uuid PRIMARY KEY,
    created_at timestamptz NOT NULL,
    updated_at timestamptz,
    content    text,
    channel_id uuid        NOT NULL,
    author_id uuid NOT NULL,
    FOREIGN KEY (channel_id)
        REFERENCES channels (id)
        ON DELETE CASCADE,
    FOREIGN KEY (author_id)
        REFERENCES users(id)
        ON DELETE SET NULL
);

CREATE TABLE message_attachments
(
    message_id    uuid NOT NULL,
    attachment_id uuid NOT NULL,
    FOREIGN KEY (message_id)
        REFERENCES messages (id)
        ON DELETE CASCADE,
    FOREIGN KEY (attachment_id)
        REFERENCES binary_contents
        ON DELETE CASCADE
);