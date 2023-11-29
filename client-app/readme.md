# Client App

## API Docs

Seluruh API yang dimiliki oleh service client sudah dibuat di Postman Collection.

## How To Start

1. Ensure port 8080, 9000 is not used and exposed
2. Run `docker compose up --build -d`

## Migration and Seeding
Ketika docker compose dijalankan, script DDL akan otomatis diexecute. Spring boot service juga akan secara otomatis melakukan injeksi data setiap kali service dinyalakan.
