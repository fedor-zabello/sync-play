CREATE TABLE user_profile_channel
(
    user_profile_id BIGINT NOT NULL,
    channel_id      BIGINT NOT NULL,
    PRIMARY KEY (user_profile_id, channel_id),
    FOREIGN KEY (user_profile_id) REFERENCES user_profile (id) ON DELETE CASCADE,
    FOREIGN KEY (channel_id) REFERENCES channel (id) ON DELETE CASCADE
);