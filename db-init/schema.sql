CREATE TABLE IF NOT EXISTS customers (
    account_number  VARCHAR2(14 CHAR) UNIQUE NOT NULL,
    name            VARCHAR2(100) NOT NULL,
    national_id     VARCHAR2(20) PRIMARY KEY,
    customer_type   VARCHAR2(20) CHECK (customer_type IN ('individual', 'corporate')) NOT NULL,
    dob             DATE NOT NULL,
    nationality     VARCHAR2(50) NOT NULL,
    phone           VARCHAR2(20) NOT NULL,
    address         VARCHAR2(255) NOT NULL,
    postal_code     VARCHAR2(20) NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP
);


CREATE TABLE IF NOT EXISTS accounts (
    account_number  VARCHAR2(14 CHAR) PRIMARY KEY,
   balance         NUMBER(18,2) DEFAULT 0 CHECK (balance >= 0),
    status          VARCHAR2(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'BLOCKED')) NOT NULL,
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



--CREATE INDEX idx_account_customer ON accounts(account_number);
CREATE INDEX idx_transaction_from ON transactions(account_from);
CREATE INDEX idx_transaction_to ON transactions(account_to);
CREATE INDEX idx_transaction_created ON transactions(created_at);



CREATE TABLE system_logs (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_type  VARCHAR2(50) NOT NULL,
    description VARCHAR2(4000),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


