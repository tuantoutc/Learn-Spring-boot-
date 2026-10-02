ALTER TABLE refresh_tokens
    ADD COLUMN used BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN family_id VARCHAR(255) NOT NULL;


-- Tạo index cho cột token (nếu token chưa được đánh UNIQUE)
CREATE INDEX IF NOT EXISTS idx_token ON refresh_tokens(token);

-- Tạo index cho cột family_id (rất quan trọng để tối ưu hàm revokeByFamilyId)
CREATE INDEX IF NOT EXISTS idx_family_id ON refresh_tokens(family_id);