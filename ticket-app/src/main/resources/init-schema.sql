CREATE TABLE event
(
    event_id          SERIAL        NOT NULL,
    event_name        VARCHAR(50)   NOT NULL,
    event_description VARCHAR(1200) NOT NULL,
    CONSTRAINT pk_event PRIMARY KEY (event_id)
);

CREATE TABLE ticket
(
    ticket_id     SERIAL       NOT NULL,
    event_id      INTEGER       NOT NULL REFERENCES event(event_id),
    seat_id       INTEGER      NOT NULL,
    seat_price    DOUBLE PRECISION NOT NULL,
    ticket_status VARCHAR(255) NOT NULL DEFAULT 'OPEN',
    CONSTRAINT pk_ticket PRIMARY KEY (ticket_id)
);

CREATE TABLE invoice
(
    id             SERIAL       NOT NULL,
    ticket_id      INTEGER       NOT NULL,
    invoice_status VARCHAR(255) NOT NULL,
    invoice_number INTEGER       NOT NULL,
    CONSTRAINT pk_invoice PRIMARY KEY (id)
);

CREATE TABLE queue
(
    id           SERIAL NOT NULL,
    ticket_id    INTEGER NOT NULL references ticket(ticket_id),
    queue_status VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_queue PRIMARY KEY (id)
);