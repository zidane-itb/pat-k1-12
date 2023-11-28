CREATE TABLE invoice
(
    id             SERIAL       NOT NULL,
    amount         double precision not null,
    invoice_status VARCHAR(255) NOT NULL,
    CONSTRAINT pk_invoice PRIMARY KEY (id)
);