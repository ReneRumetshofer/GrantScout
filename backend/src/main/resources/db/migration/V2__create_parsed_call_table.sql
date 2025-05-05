CREATE TABLE parsed_call (
    id BIGINT NOT NULL,
    json_data JSONB,

    CONSTRAINT pk_parsed_call PRIMARY KEY (id),
    CONSTRAINT fk_parsed_call_on_id FOREIGN KEY (id) REFERENCES call (id)
);