# Sistema ESCUELA ABC

Aplicación web Java 17 + Servlet API `javax.servlet` + JSP/JSTL + JDBC + MySQL, preparada como WAR para Tomcat 9. Incluye altas, bajas, cambios y consultas para las cinco tablas del esquema ESCUELA.

## Módulos
- Alumnos: nombre, edad y promedio.
- Profesores: nombre y especialidad.
- Materias: nombre y créditos.
- Profesor-Materia: asigna profesores a materias.
- Alumno-Materia: registra inscripción y calificación.

Las dos tablas de relación utilizan listas desplegables cargadas desde las tablas principales. Las operaciones usan consultas preparadas. MySQL impide borrar registros principales que aún tengan relaciones, para preservar la integridad referencial.

## Requisitos
- JDK 17
- Maven 3.8 o superior
- MySQL 8.x con el esquema `ESCUELA` creado a partir del SQL proporcionado por el profesor
- Tomcat 9.x (no Tomcat 10/11, porque este proyecto usa `javax.servlet`)

## Configurar conexión MySQL
Por defecto se usa:
- URL: `jdbc:mysql://localhost:3306/ESCUELA?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true`
- Usuario: `root`
- Contraseña: `1234`

Ajusta `src/main/java/mx/edu/escuela/DB.java` para tu entorno o define las variables de entorno `ESCUELA_DB_URL`, `ESCUELA_DB_USER` y `ESCUELA_DB_PASSWORD`. No publiques contraseñas reales en repositorios.

## Ejecutar en local
1. Crea la base de datos y las tablas con el SQL original del profesor. Ese SQL también inserta datos de ejemplo; ejecútalo una sola vez en una base limpia.
2. Revisa la conexión de `DB.java`.
3. Desde la carpeta que contiene `pom.xml`:

   ```bash
   mvn clean package
   ```

4. Copia `target/ESCUELA-ABC.war` a `TOMCAT_HOME/webapps/`.
5. Inicia Tomcat 9.
6. Abre `http://localhost:8080/ESCUELA-ABC/`.

## Publicar en Ubuntu 22.04 (guía resumida)
1. Verifica Java 17, Maven, MySQL y Tomcat 9 instalados.
2. Crea la base `ESCUELA` en el servidor y ejecuta el SQL del profesor.
3. Configura la conexión con variables de entorno o edita `DB.java` antes de compilar. Si MySQL está en la misma VM, `localhost` sirve; si está en otro servidor, configura el host y permisos de red adecuados.
4. En tu computadora compila con `mvn clean package` y copia el WAR al servidor (por ejemplo mediante `scp`).
5. Copia el WAR a `~/Workspace/apache-tomcat-9.0.123/webapps/` (ajusta la ruta a la instalación real) y reinicia Tomcat.
6. Si Apache está configurado como proxy para Tomcat en `8080`, visita la ruta que corresponda al contexto `/ESCUELA-ABC/`.

## Notas
- No es una aplicación Spring Boot; se despliega en Tomcat 9 como WAR.
- No necesita Azure Database ni otro servicio de base de datos administrado, pero sí necesita un servidor MySQL accesible para persistir los cambios.
- Para eliminar un alumno/profesor/materia que tenga relaciones, primero elimina las filas relacionadas en los módulos Alumno-Materia o Profesor-Materia.
