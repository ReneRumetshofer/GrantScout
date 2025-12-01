-- Create sequences
CREATE SEQUENCE IF NOT EXISTS chat_conversation_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS chat_message_id_seq START WITH 1 INCREMENT BY 1;

-- Create chat_conversation table
CREATE TABLE chat_conversation (
    id BIGINT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_chat_conversation PRIMARY KEY (id)
);

-- Create chat_message table
CREATE TABLE chat_message (
    id BIGINT NOT NULL,
    conversation_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_chat_message PRIMARY KEY (id),
    CONSTRAINT fk_chat_message_conversation
        FOREIGN KEY (conversation_id)
        REFERENCES chat_conversation(id)
        ON DELETE CASCADE
);

-- Create index on conversation_id for faster message retrieval
CREATE INDEX idx_chat_message_conversation_id ON chat_message(conversation_id);

-- Create index on updated_at for faster conversation list retrieval
CREATE INDEX idx_chat_conversation_updated_at ON chat_conversation(updated_at DESC);

