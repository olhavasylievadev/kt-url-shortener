-- Potential improvement would be to add expiration date, so this was we don't
-- store shortened links forever.
CREATE TABLE IF NOT EXISTS urls (
    id SERIAL PRIMARY KEY,
    original_url VARCHAR(2048) NOT NULL,
    created_at TIMESTAMP NOT NULL
);