# loginDB

Aplicación web con autenticación integrada entre Spring Boot (frontend) y Flask (backend), que permite iniciar sesión y registrarse con persistencia de datos.

## Autora

Lucía Martín Asenjo (lma1006@alu.ubu.es)
## Estructura del proyecto

```
loginDB-v2/
├── backend/              # API Flask para gestión de usuarios y credenciales
│   ├── app.py
│   └── requirements.txt
├── frontend/             # Aplicación Spring Boot (Thymeleaf + Spring Security + H2)
│   ├── src/
│   ├── pom.xml
│   └── ...
├── docker-compose.yml    # Orquestación de servicios
├── README.md             # Este archivo :)
```

---
## Ejecución con Docker Compose

> La forma más sencilla de levantar todo junto.

### 1. Construír las imágenes

```bash
docker compose build
```

### 2. Arrancar los servicios

```bash
docker compose up
```

- Flask se ejecuta en `http://localhost:5000`
- Spring Boot se ejecuta en `http://localhost:8080`

### 3. Acceder a la aplicación

Abrir [http://localhost:8080](http://localhost:8080)

---
## Ejecución desde IntelliJ con Maven

> **Requisitos previos**: Java 17+, Maven, Python 3, y Flask. Lo demás está en `backend/requeriments.txt`.

### 1. Clonar el repositorio y abrir `frontend/` como proyecto principal en IntelliJ

```bash
git clone ...
cd loginDB-v2/frontend
```

### 2. Añadir `backend/` como módulo adicional

En IntelliJ:
- `File > New > Module from Existing Sources…`
- Elegir `../backend`
- Tipo: "Python"

### 3. Ejecutar el backend desde IntelliJ

Añadir `app.py` en `Run > Edit Configurations…`.
### 4. Ejecutar el perfil `prod` en IntelliJ

Añadir el perfil `prod` para `com.loginDB.app.Application` en  `Run > Edit Configurations…`

---
## Notas técnicas

- La base de datos es `H2` en memoria (puede cambiarse fácilmente por PostgreSQL o similar).
- Los usuarios y contraseñas están protegidos con hash desde Flask (no se almacenan en texto plano).
