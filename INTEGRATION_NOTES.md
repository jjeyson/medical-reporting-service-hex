# Contratos Kafka integrados

El reporting procesa los contratos de pacientes, citas y consultas y mantiene
las vistas reconstruibles de MongoDB.

## 1. Eventos de citas

Los topics `appointment.created`, `appointment.rescheduled` y
`appointment.cancelled` actualizan `appointment_view`. Los consumidores aceptan
mensajes anteriores a esta versión; si `appointment.created` no contiene
`status`, se utiliza `CONFIRMED`.

`appointment.created` incluye además `specialty`, `reason` y `status` para que
la vista de lectura tenga los datos funcionales del registro.

## 2. Eventos de consultas

`consultation.registered` incluye `consultationDate` y `reason`. Reporting sigue
aceptando mensajes antiguos: usa `occurredAt` cuando falta la fecha.

Al completar una consulta, `medical-consultation-service` publica en
`consultation.completed`:

```json
{
  "consultationId": "820ba8cd-698d-49ad-a2ea-644bc2e4bd5d",
  "occurredAt": "2026-09-14T15:00:00Z"
}
```

Reporting consume el evento y cambia a `COMPLETED` tanto `consultation_view`
como los cálculos posteriores del dashboard.
