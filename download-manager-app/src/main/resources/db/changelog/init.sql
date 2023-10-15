--changeset author:kkochel init DB
-- cega user
CREATE SCHEMA IF NOT EXISTS "cega" AUTHORIZATION "biobank";

create table cega.cega_users
(
    id          bigint not null primary key,
    country     varchar(255),
    email       varchar(255),
    full_name   varchar(255),
    institution varchar(255),
    password    varchar(255),
    username    varchar(255)
);

CREATE UNIQUE INDEX uni_idx_username on cega.cega_users (username);

alter table cega.cega_users
    owner to biobank;

create table cega.cega_user_keys
(
    cega_user_id bigint not null
        constraint fk_cega_user_keys_to_cega_users references cega.cega_users,
    type         varchar(255),
    key          oid
);

alter table cega.cega_user_keys
    owner to biobank;

create sequence cega.cega_user_seq increment by 1;
alter sequence cega.cega_user_seq owner to biobank;


-- dataset
CREATE SCHEMA IF NOT EXISTS "dataset" AUTHORIZATION "biobank";

CREATE TABLE dataset.datasets
(
    id          bigint not null primary key,
    stable_id   varchar(255),
    title       varchar(255),
    description oid
);

CREATE UNIQUE INDEX uni_idx_dataset_stable_id on dataset.datasets (stable_id);

ALTER TABLE dataset.datasets
    OWNER TO biobank;

CREATE SEQUENCE dataset.dataset_seq INCREMENT BY 1;
ALTER SEQUENCE dataset.dataset_seq OWNER TO biobank;

CREATE TABLE dataset.files
(
    id                  bigint not null primary key,
    archive_file_size   bigint,
    decrypted_file_size bigint,
    fk_dataset          bigint,
    archive_file_path   varchar(255),
    file_name           varchar(255),
    header              varchar(255),
    stable_id           varchar(255),
    CONSTRAINT fk_dataset_id FOREIGN KEY (fk_dataset) REFERENCES dataset.datasets (id)
);

CREATE UNIQUE INDEX uni_idx_file_stable_id on dataset.files (stable_id);

ALTER TABLE dataset.files
    OWNER TO biobank;

CREATE SEQUENCE dataset.file_dataset_seq INCREMENT BY 1;
ALTER SEQUENCE dataset.file_dataset_seq OWNER TO biobank;

-- permission
CREATE SCHEMA IF NOT EXISTS "permission" AUTHORIZATION "biobank";
CREATE TABLE permission.permissions
(
    dataset_id VARCHAR(255) NOT NULL ,
    status     VARCHAR(255)
        constraint permissions_status_check
            check ((status)::text = ANY
                   ((ARRAY ['AVAILABLE'::character varying, 'REVOKED'::character varying])::text[])),
    username   VARCHAR(255) NOT NULL ,
    updated_at timestamp(6),
    primary key (dataset_id, username)
);

alter table permission.permissions
    owner to biobank;