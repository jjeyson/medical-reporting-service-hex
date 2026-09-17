# Medical Reporting Service

Microservicio de lectura CQRS construido con Quarkus 3.37.4, Java 21,
MongoDB y Kafka. La implementación está organizada con arquitectura hexagonal.

## Ejecutar en Windows 11

Requisitos: JDK 21, Kafka y MongoDB.

Puedes levantar solamente MongoDB para desarrollo con:

```powershell
docker compose -f docker-compose.mongodb.yml up -d
docker ps
```

Si tu `docker-compose.yml` compartido ya contiene MongoDB en el puerto `27017`,
no levantes este segundo archivo: utiliza un solo contenedor para evitar el
conflicto de puerto.

MongoDB queda disponible en `mongodb://localhost:27017`. No crea la base hasta
la primera escritura; al iniciar Quarkus, Liquibase MongoDB crea
`hospital_reporting`, sus colecciones, validadores, índices y el historial.

```powershell
$env:MONGO_URL="mongodb://localhost:27017"
$env:MONGO_DATABASE="hospital_reporting"
$env:KAFKA_BOOTSTRAP="localhost:9092"
$env:HTTP_PORT="9005"
.\mvnw.cmd clean test
.\mvnw.cmd quarkus:dev
```

Swagger UI: `http://localhost:9005/report/q/swagger-ui`.

## Migraciones MongoDB

MongoDB no posee tablas y Flyway no administra MongoDB. Este proyecto utiliza
la extensión oficial de Quarkus para Liquibase MongoDB:

- `quarkus-liquibase-mongodb`: integración de Quarkus.
- `src/main/resources/db/changeLog.xml`: cambios versionados.
- `DATABASECHANGELOG`: historial de cambios aplicados.
- `DATABASECHANGELOGLOCK`: bloqueo para evitar migraciones simultáneas.

Para agregar una migración, crea un `changeSet` nuevo con otro identificador.
Nunca modifiques uno aplicado; agrega uno nuevo, igual que con Flyway.

Los validadores están en modo `moderate/warn` porque las proyecciones pueden
recibir eventos fuera de orden. El historial clínico relacional continúa siendo
la fuente de verdad; MongoDB es una vista reconstruible para lectura.

Para inspeccionar colecciones, migraciones, índices y ejecutar consultas de
ejemplo:

```powershell
mongosh "mongodb://localhost:27017/hospital_reporting" .\scripts\mongo\reporting-queries.js
```

## Endpoints conservados

- `GET /report/api/reports/consultations`
- `GET /report/api/reports/patients/{patientId}/history`
- `GET /report/api/reports/dashboard`

Filtros de consultas: `patientId`, `doctorId`, `status`, `dateFrom`, `dateTo`,
`page` y `size`. Los estados válidos son `IN_PROGRESS`, `COMPLETED` y
`CANCELLED`; `size` admite de 1 a 100.

Ejemplo:

```http
GET /report/api/reports/consultations?patientId=ab402de2-3b42-47d4-ac2d-7552809cab9e&status=COMPLETED&page=0&size=20
```

## Eventos e idempotencia

Los adaptadores Kafka consumen eventos y llaman al puerto de entrada
`ProjectionEventUseCase`. Los adaptadores MongoDB:

- hacen `upsert` de pacientes, citas y consultas;
- proyectan `appointment.created`, `appointment.rescheduled` y
  `appointment.cancelled` en `appointment_view`;
- propaga cambios de nombre del paciente a consultas ya proyectadas;
- crea una proyección mínima si un evento hijo llega antes que el registro;
- evita duplicar diagnósticos, recetas y adjuntos por sus identificadores;
- ignora cambios de citas más antiguos usando `lastUpdatedAt`.

Consulta `ARCHITECTURE.md` para la distribución de responsabilidades e
`INTEGRATION_NOTES.md` para los contratos Kafka integrados.
