--changeset author:kkochel init DB

CREATE SCHEMA IF NOT EXISTS "cega_user" AUTHORIZATION "biobank";

create table cega_user.cega_users
(
    id          bigint not null,
    country     varchar(255),
    email       varchar(255),
    full_name   varchar(255),
    institution varchar(255),
    password    varchar(255),
    username    varchar(255),
    primary key (id)
);

CREATE UNIQUE INDEX uidx_username on cega_user.cega_users (username);

alter table cega_user.cega_users
    owner to biobank;

create table cega_user.cega_user_keys
(
    cega_user_id bigint not null constraint fk_cega_user_keys_to_cega_users references cega_user.cega_users,
    type         varchar(255),
    key          oid
);

alter table cega_user.cega_user_keys
    owner to biobank;

create sequence cega_user.cega_user_seq
    increment by 50;

alter sequence cega_user.cega_user_seq owner to biobank;
