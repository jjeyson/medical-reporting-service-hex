# Informe de migración

## Conservado

- Endpoints y estructura JSON de respuesta.
- Filtros, paginación y aggregation pipelines.
- Topics Kafka y contratos existentes.
- MongoDB Panache, JWT, OpenAPI, Health y Prometheus.

## Reorganizado

- Recursos REST y consumidores Kafka: adaptadores de entrada.
- Servicios: casos de uso puros, sin CDI ni MongoDB.
- Repositorio y estadísticas: puertos más adaptadores MongoDB.
- Entidades: documentos de persistencia separados del dominio.
- Wiring CDI: productores en infraestructura.

## Mejorado

- Migraciones MongoDB versionadas mediante `quarkus-liquibase-mongodb`.
- Índices compuestos alineados con los filtros reales.
- Escritura tolerante al desorden de eventos.
- Prevención de duplicados durante reintentos Kafka.
- Propagación del nombre actualizado del paciente.
- Validación de paginación y rango de fechas.
- Configuración parametrizada para desarrollo y CI/CD.
- Roles alineados con los otros microservicios: `MEDICO`, `RECEPCIONISTA`,
  `ADMINISTRADOR` y `PACIENTE`, manteniendo temporalmente los roles originales
  en inglés para no romper consumidores existentes.
