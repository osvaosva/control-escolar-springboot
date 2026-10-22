# Sistema de Control Escolar

## Descripción general
Sistema para administrar la información académica y administrativa de una
institución educativa: alumnos, profesores, asignaturas, cursos, horarios,
aulas, semestres e inscripciones. Parte del modelo de datos normalizado
(3FN) de 9 tablas diseñado en la materia de Sistemas Manejadores de Bases
de Datos Relacionales, y ahora se implementa como aplicación con Spring Boot.

## Integrantes
-Aguiñiga Olvera Alejando
-Franco Solando Juan Pablo
-Vazquez Alvarez Osvaldo

## Requerimientos (primeros 5)
| ID | Requerimiento | Descripción |
|---|---|---|
| RF-01 | Gestión de alumnos | Alta, consulta, modificación y baja de alumnos |
| RF-02 | Gestión de profesores | Registro de profesores y asignación a cursos |
| RF-03 | Catálogos de asignaturas y semestres | Registro de materias y periodos escolares |
| RF-04 | Cursos, horarios y aulas | Ofertar cursos y asignar horarios y aulas sin empalmes |
| RF-05 | Inscripciones | Inscribir alumnos a cursos validando cupo y registrar calificación |

## Funcionalidades que Spring Boot puede resolver con paquetes
| Funcionalidad | Paquete |
|---|---|
| API REST | spring-boot-starter-web |
| Conexión a base de datos | spring-boot-starter-data-jpa + driver de Oracle |
| Validación de datos | spring-boot-starter-validation |
| Autenticación y seguridad | spring-boot-starter-security |
| Logs | SLF4J / Logback |
| Documentación de la API | springdoc-openapi (Swagger) |
| Monitoreo | spring-boot-starter-actuator |
| Pruebas | spring-boot-starter-test |

## Cómo ejecutar
1. Abrir el repositorio en VS Code y elegir "Reopen in Container".
2. Ejecutar `./mvnw spring-boot:run`.
3. Abrir http://localhost:8080
