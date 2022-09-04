CREATE TABLE batch_job_instance
(
    job_instance_id BIGINT       NOT NULL,
    version         BIGINT,
    job_name        VARCHAR(100) NOT NULL,
    job_key         VARCHAR(32)  NOT NULL,
    CONSTRAINT batch_job_instance_pkey PRIMARY KEY (job_instance_id)
);

ALTER TABLE batch_job_instance
    ADD CONSTRAINT job_inst_un UNIQUE (job_name, job_key);

CREATE
SEQUENCE IF NOT EXISTS batch_job_execution_seq AS bigint START
WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

CREATE
SEQUENCE IF NOT EXISTS batch_job_seq AS bigint START
WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

CREATE
SEQUENCE IF NOT EXISTS batch_step_execution_seq AS bigint START
WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

CREATE TABLE batch_job_execution
(
    job_execution_id           BIGINT NOT NULL,
    version                    BIGINT,
    job_instance_id            BIGINT NOT NULL,
    create_time                TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    start_time                 TIMESTAMP WITHOUT TIME ZONE,
    end_time                   TIMESTAMP WITHOUT TIME ZONE,
    status                     VARCHAR(10),
    exit_code                  VARCHAR(2500),
    exit_message               VARCHAR(2500),
    last_updated               TIMESTAMP WITHOUT TIME ZONE,
    job_configuration_location VARCHAR(2500),
    CONSTRAINT batch_job_execution_pkey PRIMARY KEY (job_execution_id)
);

CREATE TABLE batch_job_execution_context
(
    job_execution_id   BIGINT        NOT NULL,
    short_context      VARCHAR(2500) NOT NULL,
    serialized_context TEXT,
    CONSTRAINT batch_job_execution_context_pkey PRIMARY KEY (job_execution_id)
);

CREATE TABLE batch_job_execution_params
(
    job_execution_id BIGINT       NOT NULL,
    type_cd          VARCHAR(6)   NOT NULL,
    key_name         VARCHAR(100) NOT NULL,
    string_val       VARCHAR(250),
    date_val         TIMESTAMP WITHOUT TIME ZONE,
    long_val         BIGINT,
    double_val FLOAT8,
    identifying      CHAR(1)      NOT NULL
);

CREATE TABLE batch_step_execution
(
    step_execution_id  BIGINT       NOT NULL,
    version            BIGINT       NOT NULL,
    step_name          VARCHAR(100) NOT NULL,
    job_execution_id   BIGINT       NOT NULL,
    start_time         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time           TIMESTAMP WITHOUT TIME ZONE,
    status             VARCHAR(10),
    commit_count       BIGINT,
    read_count         BIGINT,
    filter_count       BIGINT,
    write_count        BIGINT,
    read_skip_count    BIGINT,
    write_skip_count   BIGINT,
    process_skip_count BIGINT,
    rollback_count     BIGINT,
    exit_code          VARCHAR(2500),
    exit_message       VARCHAR(2500),
    last_updated       TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT batch_step_execution_pkey PRIMARY KEY (step_execution_id)
);

CREATE TABLE batch_step_execution_context
(
    step_execution_id  BIGINT        NOT NULL,
    short_context      VARCHAR(2500) NOT NULL,
    serialized_context TEXT,
    CONSTRAINT batch_step_execution_context_pkey PRIMARY KEY (step_execution_id)
);

CREATE TABLE hibernate_sequences
(
    sequence_name VARCHAR(255) NOT NULL,
    next_val      BIGINT,
    CONSTRAINT hibernate_sequences_pkey PRIMARY KEY (sequence_name)
);

CREATE TABLE word
(
    id             BIGINT NOT NULL,
    definition     VARCHAR(255),
    part_of_speech VARCHAR(255),
    results JSONB,
    synonyms JSONB,
    typeof JSONB,
    word           VARCHAR(255),
    CONSTRAINT word_pkey PRIMARY KEY (id)
);

ALTER TABLE batch_job_execution_context
    ADD CONSTRAINT job_exec_ctx_fk FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution (job_execution_id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE batch_job_execution_params
    ADD CONSTRAINT job_exec_params_fk FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution (job_execution_id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE batch_step_execution
    ADD CONSTRAINT job_exec_step_fk FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution (job_execution_id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE batch_job_execution
    ADD CONSTRAINT job_inst_exec_fk FOREIGN KEY (job_instance_id) REFERENCES batch_job_instance (job_instance_id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE batch_step_execution_context
    ADD CONSTRAINT step_exec_ctx_fk FOREIGN KEY (step_execution_id) REFERENCES batch_step_execution (step_execution_id) ON UPDATE NO ACTION ON DELETE NO ACTION;