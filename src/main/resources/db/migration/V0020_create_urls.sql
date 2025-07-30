-- potential improvement would be also to add expiration_date, as this way
-- we won't keep shortened URLs forever and won't let our DB grow insanely.
CREATE TABLE IF NOT EXISTS urls (
    id SERIAL PRIMARY KEY,
    original_url VARCHAR(2048) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);