CREATE TABLE account
(
    account_id SERIAL       NOT NULL,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50),
    email      VARCHAR(100) NOT NULL,
    CONSTRAINT pk_account PRIMARY KEY (account_id)
);

CREATE TABLE ticket_booking_history
(
    ticket_booking_history_id SERIAL     NOT NULL,
    ticket_id                 VARCHAR(50)                             NOT NULL,
    booking_status            VARCHAR(50) DEFAULT 'WAITING'           NOT NULL,
    account_id                BIGINT references account(account_id),
    CONSTRAINT pk_ticket_booking_history PRIMARY KEY (ticket_booking_history_id)
);