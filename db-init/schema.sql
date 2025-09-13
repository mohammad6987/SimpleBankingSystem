CREATE TABLE IF NOT EXISTS customers (
    id              NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name            VARCHAR2(100) NOT NULL,
    national_id     VARCHAR2(20) UNIQUE NOT NULL,
    customer_type   VARCHAR2(20) CHECK (customer_type IN ('individual', 'corporate')) NOT NULL,
    dob             DATE NOT NULL,
    phone           VARCHAR2(20) NOT NULL,
    address         VARCHAR2(255) NOT NULL,
    postal_code     VARCHAR2(20) NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP
);


CREATE TABLE IF NOT EXISTS accounts (
    account_number  VARCHAR2(14 CHAR) PRIMARY KEY,
    customer_id     NUMBER NOT NULL,
    balance         NUMBER(18,2) DEFAULT 0 CHECK (balance >= 0),
    status          VARCHAR2(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'BLOCKED')) NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
    CONSTRAINT chk_account_number_format CHECK (REGEXP_LIKE(account_number, '^[0-9]{14}$'))
);


CREATE TABLE IF NOT EXISTS account_history (
    id            NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    account_number VARCHAR2(14 CHAR) NOT NULL,
    field_name    VARCHAR2(50) NOT NULL,
    old_value     VARCHAR2(255),
    new_value     VARCHAR2(255),
    changed_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    changed_by    VARCHAR2(50),
    CONSTRAINT fk_history_account FOREIGN KEY (account_number) REFERENCES accounts(account_number)
);


CREATE TABLE transactions (
    id              NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tracking_code   VARCHAR2(50) UNIQUE NOT NULL,
    type            VARCHAR2(20) CHECK (type IN ('deposit', 'withdraw', 'transfer')) NOT NULL,
    account_from    VARCHAR2(20),
    account_to      VARCHAR2(20),
    amount          NUMBER(18,2) NOT NULL CHECK (amount > 0),
    fee             NUMBER(18,2) DEFAULT 0 CHECK (fee >= 0),
    status          VARCHAR2(20) CHECK (status IN ('pending', 'success', 'failed')) NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_account_from FOREIGN KEY (account_from) REFERENCES accounts(account_number),
    CONSTRAINT fk_account_to FOREIGN KEY (account_to) REFERENCES accounts(account_number)
);


CREATE TABLE fee_config (
    id              NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    min_fee         NUMBER(18,2) NOT NULL,
    max_fee         NUMBER(18,2) NOT NULL,
    percent_fee     NUMBER(5,2) NOT NULL CHECK (percent_fee >= 0 AND percent_fee <= 100)
);



CREATE INDEX idx_account_customer ON accounts(customer_id);
CREATE INDEX idx_transaction_from ON transactions(account_from);
CREATE INDEX idx_transaction_to ON transactions(account_to);
CREATE INDEX idx_transaction_created ON transactions(created_at);



CREATE TABLE system_logs (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_type  VARCHAR2(50) NOT NULL,
    description VARCHAR2(4000),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



INSERT INTO customers (name, national_id, customer_type, dob, phone, address, postal_code)
VALUES ('Ali Rezaei', '1234567890', 'individual', TO_DATE('1990-05-15', 'YYYY-MM-DD'), '09123456789', 'Tehran, Iran', '12345');

INSERT INTO customers (name, national_id, customer_type, dob, phone, address, postal_code)
VALUES ('Sara Mohammadi', '9876543210', 'individual', TO_DATE('1992-08-20', 'YYYY-MM-DD'), '09198765432', 'Mashhad, Iran', '54321');

INSERT INTO customers (name, national_id, customer_type, dob, phone, address, postal_code)
VALUES ('Parsian Corp', '1122334455', 'corporate', TO_DATE('2005-01-01', 'YYYY-MM-DD'), '02112345678', 'Tehran, Iran', '11111');

COMMIT;


INSERT INTO accounts (account_number, customer_id, balance, status)
VALUES ('AC001', 1, 1000, 'active');

INSERT INTO accounts (account_number, customer_id, balance, status)
VALUES ('AC002', 2, 500, 'active');

INSERT INTO accounts (account_number, customer_id, balance, status)
VALUES ('AC003', 3, 10000, 'active');

COMMIT;
