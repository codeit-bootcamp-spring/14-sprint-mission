create table binary_contents (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    bytes BYTEA NOT NULL
);

create table users (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,
    profile_id UUID UNIQUE,
    FOREIGN KEY (profile_id) REFERENCES binary_contents(id) ON DELETE SET NULL
);

create table channels (
     id UUID PRIMARY KEY,
     created_at TIMESTAMPTZ NOT NULL,
     updated_at TIMESTAMPTZ,
     name VARCHAR(100),
     description VARCHAR(500),
     type VARCHAR(10) NOT NULL CHECK ( type IN ('PUBLIC','PRIVATE'))
);

create table user_statuses (
      id UUID PRIMARY KEY,
      created_at TIMESTAMPTZ NOT NULL,
      updated_at TIMESTAMPTZ,
      suer_id UUID NOT NULL UNIQUE,
      FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
      last_active_at TIMESTAMPTZ NOT NULL
);

create table read_statuses (
       id UUID PRIMARY KEY,
       created_at TIMESTAMPTZ NOT NULL,
       updated_at TIMESTAMPTZ,
       user_id UUID NOT NULL,
       channel_id UUID NOT NULL,
       last_read_At TIMESTAMPTZ NOT NULL,
       UNIQUE (user_id, channel_id),
       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
       FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE
);

create table messages (
       id UUID PRIMARY KEY,
       created_at TIMESTAMPTZ NOT NULL,
       updated_at TIMESTAMPTZ,
       content TEXT,
       channel_id UUID NOT NULL,
       author_id UUID,
       FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE,
       FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE SET NULL
);

create table message_attachments (
       message_id UUID NOT NULL,
       attachment_id UUID NOT NULL,
       FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE,
       FOREIGN KEY (attachment_id) REFERENCES binary_contents(id) ON DELETE CASCADE,
       PRIMARY KEY (message_id, attachment_id)
);
