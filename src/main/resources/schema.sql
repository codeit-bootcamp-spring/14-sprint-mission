DROP TABLE IF EXISTS message_attachments CASCADE;
DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS read_statuses CASCADE;
DROP TABLE IF EXISTS user_statuses CASCADE;
DROP TABLE IF EXISTS channels CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS binary_contents CASCADE;
DROP TYPE IF EXISTS channel_type CASCADE;

CREATE TABLE binary_contents (
                                 id           UUID PRIMARY KEY,
                                 created_at   TIMESTAMPTZ NOT NULL,
                                 file_name    VARCHAR(255) NOT NULL,
                                 size         BIGINT NOT NULL,
                                 content_type VARCHAR(100) NOT NULL

);


CREATE TABLE users (
                       id         UUID PRIMARY KEY,
                       created_at TIMESTAMPTZ NOT NULL,
                       updated_at TIMESTAMPTZ,
                       username   VARCHAR(50) NOT NULL UNIQUE,
                       email      VARCHAR(100) NOT NULL UNIQUE,
                       password   VARCHAR(60) NOT NULL,
                       profile_id UUID UNIQUE
                           REFERENCES binary_contents(id) ON DELETE SET NULL
);


CREATE TABLE channels (
                          id          UUID PRIMARY KEY,
                          created_at  TIMESTAMPTZ NOT NULL,
                          updated_at  TIMESTAMPTZ,
                          name        VARCHAR(100),
                          description VARCHAR(500),
                          type        VARCHAR(10) NOT NULL
                              CHECK (type IN ('PUBLIC', 'PRIVATE'))
);


CREATE TABLE user_statuses (
                               id             UUID PRIMARY KEY,
                               created_at     TIMESTAMPTZ NOT NULL,
                               updated_at     TIMESTAMPTZ,
                               user_id        UUID NOT NULL UNIQUE
                                   REFERENCES users(id) ON DELETE CASCADE,
                               last_active_at TIMESTAMPTZ NOT NULL
);


CREATE TABLE read_statuses (
                               id           UUID PRIMARY KEY,
                               created_at   TIMESTAMPTZ NOT NULL,
                               updated_at   TIMESTAMPTZ,
                               user_id      UUID NOT NULL
                                   REFERENCES users(id) ON DELETE CASCADE,
                               channel_id   UUID NOT NULL
                                   REFERENCES channels(id) ON DELETE CASCADE,
                               last_read_at TIMESTAMPTZ NOT NULL,
                               UNIQUE (user_id, channel_id)
);


CREATE TABLE messages (
                          id         UUID PRIMARY KEY,
                          created_at TIMESTAMPTZ NOT NULL,
                          updated_at TIMESTAMPTZ,
                          content    TEXT,
                          channel_id UUID NOT NULL
                              REFERENCES channels(id) ON DELETE CASCADE,
                          author_id  UUID
                                                 REFERENCES users(id) ON DELETE SET NULL
);


CREATE TABLE message_attachments (
                                     message_id    UUID NOT NULL
                                         REFERENCES messages(id) ON DELETE CASCADE,
                                     attachment_id UUID NOT NULL
                                         REFERENCES binary_contents(id) ON DELETE CASCADE,
                                     PRIMARY KEY (message_id, attachment_id)
);