CREATE TABLE IF NOT EXISTS google_oauth (
    id BIGINT PRIMARY KEY,
    oauth_id VARCHAR(255),
    user_profile_id BIGINT,
    FOREIGN KEY (user_profile_id) REFERENCES user_profile(id)
);
