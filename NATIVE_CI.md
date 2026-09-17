# CI/CD nativo con GitHub Actions

El workflow `.github/workflows/native-ci.yml` mantiene el modelo imperativo y
bloqueante de la aplicación. Solamente cambia el formato de compilación: genera
un ejecutable nativo Linux AMD64 y, para los tags, una imagen OCI nativa.

## Comportamiento

| Evento | Pruebas JVM | Binario nativo | Imagen GHCR | GitHub Release |
|---|---:|---:|---:|---:|
| Pull request | Sí | No | No | No |
| Push a rama no predeterminada | Sí | No | No | No |
| Push a rama predeterminada | Sí | Sí | No | No |
| Ejecución manual | Sí | Sí | No | No |
| Tag `v*` | Sí | Sí | Sí | Sí |

La compilación nativa se realiza dentro de un contenedor Mandrel/GraalVM
administrado por Quarkus. El runner de GitHub solamente necesita Java 21,
Maven Wrapper y Docker.

## Primera ejecución

1. Copiar el proyecto a un repositorio GitHub.
2. Confirmar que `mvnw` conserva permiso de ejecución:

   ```bash
   git update-index --chmod=+x mvnw
   ```

3. Subir una rama y revisar la pestaña **Actions**.
4. En la rama predeterminada, descargar el artefacto
   `native-runner-<commit>`.
5. Para publicar una versión:

   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

El tag publica:

- el ejecutable Linux AMD64 en GitHub Releases;
- `ghcr.io/<owner>/<repository>:v1.0.0`;
- `ghcr.io/<owner>/<repository>:sha-<commit>`;
- `ghcr.io/<owner>/<repository>:latest`.

## Compilación local equivalente

Desde Windows con Docker Desktop activo:

```powershell
.\mvnw.cmd clean test

.\mvnw.cmd clean package `
  -Dnative `
  -DskipTests `
  -Dquarkus.native.container-build=true
```

El ejecutable queda en:

```text
target/*-runner
```

La imagen se construye después con:

```powershell
docker build `
  -f src/main/docker/Dockerfile.native `
  -t nombre-servicio:native .
```

## Permisos y secretos

El workflow declara:

```yaml
permissions:
  contents: write
  packages: write
```

solamente en el job de publicación. Usa `secrets.GITHUB_TOKEN`, generado por
GitHub para cada ejecución. No se debe crear ni guardar un PAT dentro del
repositorio.

Las credenciales de PostgreSQL, Kafka, MongoDB, JWT o MinIO no participan en la
compilación nativa. Se configuran en la plataforma de despliegue al ejecutar la
imagen.

## Alcance

Este pipeline entrega el binario y la imagen. El despliegue automático a Azure
Container Apps se agregará como un job posterior cuando existan:

- Azure Container Registry o decisión de consumir directamente desde GHCR;
- resource group y Container Apps Environment;
- nombres definitivos de cada Container App;
- credenciales federadas de Azure;
- secretos y variables de producción.

