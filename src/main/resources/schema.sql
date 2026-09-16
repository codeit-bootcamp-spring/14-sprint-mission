DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS read_statuses CASCADE;
DROP TABLE IF EXISTS user_statuses CASCADE;
DROP TABLE IF EXISTS channels CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS binary_contents CASCADE;
DROP TABLE IF EXISTS message_attachment CASCADE;

create table binary_contents
(
    id           UUID PRIMARY KEY,
    created_at   timestamptz  not null,
    file_name    varchar(255) not null,
    size         bigint       not null,
    content_type varchar(100) not null
);

create table users
(
    id         UUID PRIMARY KEY,
    created_at timestamptz  not null,
    updated_at timestamptz,
    username   varchar(50)  not null UNIQUE,
    email      varchar(100) not null UNIQUE,
    password   varchar(60)  not null,
    profile_id uuid unique  references binary_contents (id) on DELETE set null
);

create table user_statuses
(
    id             UUID PRIMARY KEY,
    created_at     timestamptz not null,
    updated_at     timestamptz,
    user_id        uuid        not null unique references users (id) on DELETE CASCADE,
    last_active_at timestamptz not null
);

create table channels
(
    id          UUID PRIMARY KEY,
    created_at  timestamptz not null,
    updated_at  timestamptz,
    name        varchar(100),
    description varchar(500),
    type        varchar(10) not null
);

create table read_statuses
(
    id           UUID PRIMARY KEY,
    created_at   timestamptz not null,
    updated_at   timestamptz,
    user_id      UUID        not null references users (id) on DELETE CASCADE,
    channel_id   UUID        not null references channels (id) on DELETE cascade,
    last_read_at timestamptz not null,
    unique (user_id, channel_id)
);

create table messages
(
    id         UUID PRIMARY KEY,
    created_at timestamptz not null,
    updated_at timestamptz,
    content    text,
    channel_id UUID        not null references channels (id) on DELETE cascade,
    author_id  UUID        references users (id) on delete set null
);

create table message_attachment
(
    message_id    UUID not null references messages (id) on delete CASCADE,
    attachment_id UUID not null references binary_contents (id) on DELETE CASCADE
);