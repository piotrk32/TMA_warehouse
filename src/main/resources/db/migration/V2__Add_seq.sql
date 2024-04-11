ALTER TABLE requests ADD COLUMN IF NOT EXISTS request_row_id BIGINT NOT NULL DEFAULT nextval('item_row_id_seq');
