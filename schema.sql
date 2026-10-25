CREATE TABLE binary_content (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP(6) WITH TIME ZONE,
    file_name VARCHAR(255),
    size BIGINT,
    content_type VARCHAR(100),
    bytes BYTEA
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP(6) WITH TIME ZONE,
    updated_at TIMESTAMP(6) WITH TIME ZONE,
    username VARCHAR(50),
    email VARCHAR(100),
    password VARCHAR(60),
    profile_id UUID UNIQUE REFERENCES binary_content(id) ON DELETE SET NULL
);

CREATE TABLE channel (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP(6) WITH TIME ZONE,
    updated_at TIMESTAMP(6) WITH TIME ZONE,
    type VARCHAR(255) CHECK (type IN ('PUBLIC', 'PRIVATE')),
    name VARCHAR(255),
    description VARCHAR(255)
);

CREATE TABLE message (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP(6) WITH TIME ZONE,
    updated_at TIMESTAMP(6) WITH TIME ZONE,
    content VARCHAR(255),
    channel_id UUID NOT NULL REFERENCES channel(id) ON DELETE CASCADE,
    author_id UUID NOT NULL REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE message_attachments (
    id UUID PRIMARY KEY,
    message_id UUID NOT NULL REFERENCES message(id) ON DELETE CASCADE,
    attachment_id UUID NOT NULL REFERENCES binary_content(id) ON DELETE CASCADE
);

CREATE TABLE read_status (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP(6) WITH TIME ZONE,
    updated_at TIMESTAMP(6) WITH TIME ZONE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    channel_id UUID NOT NULL REFERENCES channel(id) ON DELETE CASCADE,
    last_read_at TIMESTAMP(6) WITH TIME ZONE
);

CREATE TABLE user_status (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP(6) WITH TIME ZONE,
    updated_at TIMESTAMP(6) WITH TIME ZONE,
    user_id UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    last_active_at TIMESTAMP(6) WITH TIME ZONE
);
