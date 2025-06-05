# loginDB

Aplicación web con autenticación integrada entre Spring Boot (frontend) y Flask (backend), que permite iniciar sesión y registrarse con persistencia de datos.

## 📁 Estructura del proyecto

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

## 🚀 Ejecución desde IntelliJ con Maven

> **Requisitos previos**: Java 17+, Maven, Python 3, Flask, y haber añadido correctamente el módulo `backend/` como módulo adicional en IntelliJ.

### 1. Clona el repositorio y abre `frontend/` como proyecto principal en IntelliJ

```bash
git clone ...
cd loginDB-v2/frontend
```

### 2. Añade `backend/` como módulo adicional (solo una vez)

En IntelliJ:
- `File > New > Module from Existing Sources…`
- Elige `../backend`
- Tipo: "Python" (o simplemente incluirlo como recurso)

### 3. Asegúrate de que el backend está corriendo

Desde consola (en `backend/`):

```bash
cd ../backend
python app.py
```

Deberías ver algo como: `Running on http://127.0.0.1:5000`

### 4. Configura el perfil `prod` en IntelliJ (solo si usas `application-prod.properties`)

En IntelliJ:
- `Run > Edit Configurations…`
- Añadir en VM Options:
  ```
  -Dspring.profiles.active=prod
  ```

> Alternativamente, añade el valor directamente a `application.properties` si prefieres no usar perfiles.

### 5. Ejecuta el frontend desde IntelliJ

Pulsa ▶️ sobre la clase `Application.java`.

---

## 🐳 Ejecución con Docker Compose

> Esta es la forma más sencilla de levantar todo junto, listo para producción local.

### 1. Construye las imágenes

```bash
docker compose build
```

### 2. Arranca los servicios

```bash
docker compose up
```

Esto ejecuta:
- Flask en `http://localhost:5000`
- Spring Boot en `http://localhost:8080`

### 3. Accede a la aplicación

Abre tu navegador en: [http://localhost:8080](http://localhost:8080)

---

## 🛠️ Notas técnicas

- Spring Boot utiliza `@Value("${flask.api.url}")` para conectarse al backend.
  - En Docker, se pasa como variable de entorno desde `docker-compose.yml`.
  - En IntelliJ/Maven, asegúrate de definirlo vía `application.properties` o como propiedad del sistema.
- La base de datos es `H2` en memoria (puede cambiarse fácilmente por PostgreSQL o similar).
- Los usuarios y contraseñas están protegidos con hash desde Flask (no se almacenan en texto plano).

---

## 📦 Variables de entorno relevantes

| Variable         | Descripción                     | Valor por defecto (Docker)     |
|------------------|----------------------------------|-------------------------------|
| `FLASK_ENV`      | Modo de ejecución del backend    | `development`                |
| `FLASK_RUN_PORT` | Puerto del backend Flask         | `5000`                        |
| `FLASK_API_URL`  | URL de conexión desde Spring     | `http://backend:5000`        |

---

## ✨ Futuras mejoras

- Cambiar contraseña desde `/account`
- Imagen de perfil editable
- Sustitución de H2 por PostgreSQL en producción
- Tests unitarios para la API Flask

---

## 🧃 Autoría

Proyecto desarrollado por Lucía para la asignatura de Sistemas Distribuidos (Universidad de Burgos).