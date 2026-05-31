ALTER TABLE payment_transaction

ALTER COLUMN amount
TYPE NUMERIC(19,2)

USING amount::numeric;