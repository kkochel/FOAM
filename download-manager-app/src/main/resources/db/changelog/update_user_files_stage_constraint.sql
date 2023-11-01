ALTER TABLE export.users_files
    DROP constraint export_log_export_stage,
    ADD constraint export_log_export_stage
        check ((export_stage)::text = ANY
               ((ARRAY [
                   'ACCEPTED'::character varying,
                   'RE_ENCRYPTION'::character varying,
                   'TRANSFER'::character varying,
                   'READY'::character varying,
                   'DELETED'::character varying,
                   'REVOKED'::character varying,
                   'FAILED'::character varying])::text[]))