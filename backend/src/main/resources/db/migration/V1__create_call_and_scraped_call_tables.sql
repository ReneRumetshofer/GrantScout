CREATE SEQUENCE IF NOT EXISTS call_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE call
(
    id        BIGINT        NOT NULL,
    url       VARCHAR(1024) NOT NULL,
    institute VARCHAR(255)  NOT NULL,
    status    VARCHAR(255)  NOT NULL,
    CONSTRAINT pk_call PRIMARY KEY (id)
);

CREATE TABLE scraped_call
(
    id         BIGINT                      NOT NULL,
    scraped_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    content    TEXT,
    CONSTRAINT pk_scraped_call PRIMARY KEY (id)
);

ALTER TABLE call
    ADD CONSTRAINT UQ_call_url UNIQUE (url);

ALTER TABLE scraped_call
    ADD CONSTRAINT FK_SCRAPED_CALL_ON_ID FOREIGN KEY (id) REFERENCES call (id);