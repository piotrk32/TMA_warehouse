CREATE TABLE IF NOT EXISTS user_roles (
                            user_id BIGINT NOT NULL,
                            role VARCHAR(255) NOT NULL,
                            CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id)
                                REFERENCES users (id) ON DELETE CASCADE
);

-- Create an index for better query performance
CREATE INDEX idx_user_roles_user_id ON user_roles (user_id);