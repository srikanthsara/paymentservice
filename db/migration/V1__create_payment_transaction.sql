CREATE TABLE payment_transaction (

    payment_id BIGSERIAL PRIMARY KEY,

    order_id BIGINT,

    customer_id VARCHAR(50),

    amount NUMERIC(12,2),

    payment_type VARCHAR(50),

    payment_provider VARCHAR(50),

    payment_status VARCHAR(50),

    transaction_id VARCHAR(255),

    failure_reason VARCHAR(500),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);