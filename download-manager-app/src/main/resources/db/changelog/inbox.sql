--changeset author:kkochel inbox table
CREATE SCHEMA IF NOT EXISTS "inbox" AUTHORIZATION "biobank";

CREATE TABLE inbox.inbox_messages
(
    id          bigint       NOT NULL PRIMARY KEY,
    message_id  varchar(255) NOT NULL,
    queue_name  varchar(100) NOT NULL,
    payload     text         NOT NULL,
    received_at timestamp(6) NOT NULL
);

CREATE UNIQUE INDEX uni_idx_inbox_message_id ON inbox.inbox_messages (message_id, queue_name);

ALTER TABLE inbox.inbox_messages
    OWNER TO biobank;

CREATE SEQUENCE inbox.inbox_seq INCREMENT BY 1;
ALTER SEQUENCE inbox.inbox_seq OWNER TO biobank;
