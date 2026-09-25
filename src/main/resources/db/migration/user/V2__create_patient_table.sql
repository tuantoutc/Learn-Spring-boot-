CREATE TABLE IF NOT EXISTS patients(
    id         BIGSERIAL PRIMARY KEY,
    citizen_id VARCHAR(12),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    full_name  VARCHAR(100)
);
