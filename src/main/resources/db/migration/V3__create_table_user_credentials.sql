CREATE TABLE IF NOT EXISTS user_credentials (
    id BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255),
    user_profile_id BIGINT,
    FOREIGN KEY (user_profile_id) REFERENCES user_profile(id)
);
