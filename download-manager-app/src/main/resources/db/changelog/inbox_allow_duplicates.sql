--changeset author:kkochel inbox allow duplicate messages
DROP INDEX inbox.uni_idx_inbox_message_id;

ALTER TABLE inbox.inbox_messages
    ADD COLUMN duplicate boolean NOT NULL DEFAULT false;
