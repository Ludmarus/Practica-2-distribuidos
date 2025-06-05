# loginDB - Proyecto de Autenticación Web Distribuida

Este proyecto es una aplicación web basada en Spring Boot (Java) y una API en Flask (Python), que permite gestionar login y creación de cuentas con almacenamiento seguro de credenciales.

## 📦 Estructura del Proyecto

- `src/main/java`: Código Java (Spring Boot)
- `src/main/resources/templates`: Plantillas HTML (Thymeleaf)
- `src/main/resources/static`: Recursos estáticos (CSS, JS, fuentes)
- `src/main/resources/application.properties`: Configuración de Spring
- `pom.xml`: Proyecto Maven
- `README.md`: Este archivo 😉

## 🧰 Requisitos Previos

- Java 17+
- Maven 3.6+
- Python 3.9+
- Pipenv o entorno virtual para Python
- IntelliJ IDEA (opcional pero recomendado)

## 🚀 Importación en IntelliJ IDEA

1. Abre IntelliJ.
2. Selecciona `File > Open...` y elige la carpeta raíz del proyecto (donde está `pom.xml`).
3. IntelliJ detectará automáticamente que es un proyecto Maven y lo configurará.
4. Espera a que termine de indexar y descarga las dependencias si lo requiere.
5. Puedes correr la app desde `Application.java` (botón ▶️) o desde consola.

## 🔄 Ejecución desde línea de comandos

### Backend Java (Spring Boot):

```bash
cd Practica-2-distribuidos
./mvnw spring-boot:run
```

O si no tienes el wrapper:

```bash
mvn spring-boot:run
```

### API Python (Flask):

```bash
cd flask-api
pip install -r requirements.txt
python app.py
```

Asegúrate de que el puerto del backend Flask esté configurado correctamente y sea accesible desde la app Java.

## ✨ Notas

- El endpoint `/test-api` está actualmente en construcción.
- Las contraseñas se almacenan con hash seguro.
- Se ha evitado mantener archivos de configuración locales para favorecer la portabilidad.