# Payment App

## API Docs

API yang didefinisikan disini adalah endpoint yang tidak boleh diakses secara langsung, seperti melakukan API call pada Postman. Sisanya boleh dilihat di dokumentasi Postman.

### HTTP APIs

| HTTP Method  | Endpoint    | Description              |
| -----------  | ----------  | ------------------------ |
| POST         | /v1/payment | Initiate a payment       |

## How To Start

1. Ensure port 9002, 8082 is not used and exposed
2. Run `docker compose up --build -d`

## Migration and Seeding
Ketika docker compose dijalankan, script DDL akan otomatis diexecute. Spring boot service juga akan secara otomatis melakukan injeksi data setiap kali service dinyalakan.
