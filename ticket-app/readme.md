# Ticket App

## API Docs

API yang didefinisikan disini adalah endpoint yang tidak boleh diakses secara langsung, seperti melakukan API call pada Postman. Sisanya boleh dilihat di dokumentasi Postman.

### HTTP APIs

| HTTP Method  | Endpoint                               | Description                                |
| -----------  | ----------                             | ------------------------                   |
| POST         | /v1/ticket/status-webhook              | Webhook update status dari Payment service |
| POST         | /v1/event/{eventId}/ticket/{ticketId}  | Melakukan hold tiket (dari client service) |

## How To Start

1. Ensure port 40000-40009, 20-21, 9092, 9001, 8081 is not used and exposed
2. Run `docker compose up --build -d`

## Migration and Seeding
Ketika docker compose dijalankan, script DDL akan otomatis diexecute. Spring boot service juga akan secara otomatis melakukan injeksi data setiap kali service dinyalakan.
