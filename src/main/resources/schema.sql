create table if not exists binary_contents
(
    id           UUID         NOT NULL,
    created_at   TIMESTAMPTZ  NOT NULL,
    file_name    VARCHAR(255) NOT NULL,
    size         BIGINT       NOT NULL,
    content_type VARCHAR(100) NOT NULL,

    CONSTRAINT pk_binary_contents
        primary key (id)
);

create table if not exists users
(
    id         UUID         NOT NULL,
    profile_id UUID,
    updated_at TIMESTAMPTZ,
    username   VARCHAR(50)  NOT NULL,
    password   VARCHAR(60)  NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL,
    email      VARCHAR(100) NOT NULL,

    CONSTRAINT pk_users
        primary key (id),
    CONSTRAINT fk_users_profile
        FOREIGN KEY (profile_id)
            REFERENCES binary_contents (id)
            ON DELETE SET NULL,
    CONSTRAINT uk_users_username
        UNIQUE (username),
    CONSTRAINT uk_users_email
        UNIQUE (email),
    CONSTRAINT uk_users_profile
        UNIQUE (profile_id)
);

create table if not exists user_statuses
(
    id             UUID        NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL,
    updated_at     TIMESTAMPTZ,
    user_id        UUID        NOT NULL,
    last_active_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT pk_user_statuses
        primary key (id),
    CONSTRAINT uk_user_statuses_user
        UNIQUE (user_id),
    CONSTRAINT fk_user_statuses_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);

create table if not exists channels
(
    id          UUID        NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL,
    updated_at  TIMESTAMPTZ,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        VARCHAR(10) NOT NULL,

    CONSTRAINT pk_channels
        primary key (id),
    CONSTRAINT ck_channels_type
        CHECK ( type IN ('PUBLIC', 'PRIVATE') )
);

create table if not exists read_statuses
(
    id           UUID        NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL,
    updated_at   TIMESTAMPTZ,
    user_id      UUID        NOT NULL,
    channel_id   UUID        NOT NULL,
    last_read_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT pk_read_statuses
        primary key (id),
    CONSTRAINT uk_read_statuses_user_channel
        UNIQUE (user_id, channel_id),
    CONSTRAINT fk_read_statuses_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_read_statuses_channel
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE
);

create table if not exists messages
(
    id         UUID        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    content    TEXT,
    channel_id UUID        NOT NULL,
    author_id  UUID,

    CONSTRAINT pk_messages
        PRIMARY KEY (id),
    CONSTRAINT fk_messages_channel
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_messages_author
        FOREIGN KEY (author_id)
            REFERENCES users (id)
            ON DELETE SET NULL
);

create table if not exists message_attachments
(
    message_id    UUID NOT NULL,
    attachment_id UUID NOT NULL,
    CONSTRAINT pk_message_attachments
        PRIMARY KEY (message_id, attachment_id),
    CONSTRAINT fk_message_attachments_message
        FOREIGN KEY (message_id)
            REFERENCES messages (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_message_attachments_attachment
        FOREIGN KEY (attachment_id)
            REFERENCES binary_contents (id)
            ON DELETE CASCADE
);