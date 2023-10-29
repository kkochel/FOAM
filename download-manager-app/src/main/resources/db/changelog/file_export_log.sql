-- changeset author:kkochel file export log
CREATE SCHEMA IF NOT EXISTS "export" AUTHORIZATION "biobank";

CREATE TABLE export.export_log
(
    id           bigint not null primary key,
    stable_id    varchar(20),
    username     varchar(255),
    uuid         uuid,
    created_at   timestamp(6),
    export_stage VARCHAR(20)
        constraint export_log_export_stage
            check ((export_stage)::text = ANY
                   ((ARRAY [
                       'ACCEPTED'::character varying,
                       'RE_ENCRYPTION'::character varying,
                       'TRANSFER'::character varying,
                       'READY'::character varying,
                       'DELETED'::character varying,
                       'FAILED'::character varying])::text[]))
);

ALTER TABLE export.export_log
    OWNER TO biobank;

CREATE SEQUENCE export.export_log_seq INCREMENT BY 1;
ALTER SEQUENCE export.export_log_seq OWNER TO biobank;

CREATE TABLE export.users_files
(
    id           bigint not null primary key,
    dataset_id   varchar(15),
    stable_id    varchar(15),
    username     varchar(255),
    export_stage VARCHAR(20)
        constraint export_log_export_stage
            check ((export_stage)::text = ANY
                   ((ARRAY [
                       'ACCEPTED'::character varying,
                       'RE_ENCRYPTION'::character varying,
                       'TRANSFER'::character varying,
                       'READY'::character varying,
                       'DELETED'::character varying,
                       'FAILED'::character varying])::text[]))
);

ALTER TABLE export.export_log
    OWNER TO biobank;

CREATE SEQUENCE export.users_files_seq INCREMENT BY 1;
ALTER SEQUENCE export.users_files_seq OWNER TO biobank;