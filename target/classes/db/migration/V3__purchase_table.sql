-- V1__create_purchases_table.sql
-- Create purchases table with FKs to users and products

CREATE TABLE IF NOT EXISTS purchases (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL,
  purchased_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  CONSTRAINT uq_purchases_user_product UNIQUE (user_id, product_id, purchased_at) -- optional: prevents identical duplicate purchase moments
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes (helpful for queries)
CREATE INDEX idx_purchases_user_id ON purchases (user_id);
CREATE INDEX idx_purchases_product_id ON purchases (product_id);
CREATE INDEX idx_purchases_purchased_at ON purchases (purchased_at);

-- Foreign key constraints
-- When a user is deleted, cascade delete purchases (matches cascade on User.purchases).
ALTER TABLE purchases
  ADD CONSTRAINT fk_purchases_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

-- For products, restrict deletion by default to avoid losing purchase history.
ALTER TABLE purchases
  ADD CONSTRAINT fk_purchases_product
    FOREIGN KEY (product_id) REFERENCES products(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;
