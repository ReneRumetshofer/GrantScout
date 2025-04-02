CREATE SEQUENCE IF NOT EXISTS call_id_seq;

CREATE TABLE IF NOT EXISTS call (
    id BIGINT DEFAULT nextval('call_id_seq'),
    url VARCHAR(1024) NOT NULL,
    institute VARCHAR(255) NOT NULL,
    scraped BOOLEAN NOT NULL DEFAULT FALSE,
    content TEXT,

    CONSTRAINT pk_call PRIMARY KEY (id),
    CONSTRAINT uk_call_url UNIQUE (url)
)