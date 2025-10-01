-- Enable pgcrypto for gen_random_uuid()
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Idempotent drops for dev; TODO: remove in prod
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'messages') THEN
    EXECUTE 'DROP TABLE messages CASCADE';
  END IF;
  IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'conversations') THEN
    EXECUTE 'DROP TABLE conversations CASCADE';
  END IF;
  IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'agents') THEN
    EXECUTE 'DROP TABLE agents CASCADE';
  END IF;
END $$;

-- Agents
CREATE TABLE agents (
  id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  name           TEXT        NOT NULL,
  context        TEXT        NOT NULL,
  first_message  TEXT        NOT NULL,
  response_shape JSONB       NOT NULL,
  instructions   TEXT        NOT NULL
);

-- Conversations
CREATE TABLE conversations (
  id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  agent_id    UUID NOT NULL REFERENCES agents(id)
                ON UPDATE CASCADE ON DELETE CASCADE,
  created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_conversations_agent_created
  ON conversations(agent_id, created_at DESC);

-- Messages
-- One row per utterance (role + content)
CREATE TABLE messages (
  id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  conversation_id  UUID NOT NULL REFERENCES conversations(id)
                     ON UPDATE CASCADE ON DELETE CASCADE,
  created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  role             TEXT NOT NULL CHECK (role IN ('user','agent','system')),
  content          TEXT NOT NULL
);

CREATE INDEX idx_messages_conv_created
  ON messages(conversation_id, created_at DESC);
