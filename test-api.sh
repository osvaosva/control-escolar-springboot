#!/usr/bin/env bash
# ============================================================
# Prueba de punta a punta de la API de control escolar
#
# Uso (con la API ya arrancada en OTRA terminal):
#   export DEMO_ADMIN_PASSWORD="la-misma-que-usaste-al-arrancar"
#   bash test-api.sh
#
# Recomendado: reinicia la API antes de correrlo (la base H2 en
# memoria queda limpia) para evitar choques con datos previos.
# ============================================================

BASE="${BASE:-http://localhost:8080}"
ADMIN_PASS="${DEMO_ADMIN_PASSWORD:?Falta definir DEMO_ADMIN_PASSWORD (la misma contrasena con la que arrancaste la API)}"
JSON='Content-Type: application/json'
ok=0
fallas=0

# check "descripcion" esperado obtenido
check() {
  if [ "$2" = "$3" ]; then
    echo "  OK     $1  (HTTP $3)"
    ok=$((ok + 1))
  else
    echo "  FALLA  $1  (esperaba $2 y obtuvo $3)"
    fallas=$((fallas + 1))
  fi
}

# Devuelve solo el codigo HTTP. Uso: code [opciones de curl] URL
code() { curl -s -o /dev/null -w "%{http_code}" "$@"; }

# Devuelve el cuerpo de la respuesta. Uso: body [opciones de curl] URL
body() { curl -s "$@"; }

# Extrae un numero de un campo JSON. Uso: campo '{"id":5}' id  ->  5
campo() { echo "$1" | sed -E "s/.*\"$2\":([0-9]+).*/\1/"; }

# Obtiene el token de un usuario. Uso: token usuario contrasena
token() {
  body -X POST "$BASE/auth/login" -H "$JSON" \
    -d "{\"username\":\"$1\",\"password\":\"$2\"}" \
    | sed -E 's/.*"accessToken":"([^"]+)".*/\1/'
}

R=$RANDOM
USUARIO="estudiante$R"
CLAVE_USER="clave-segura-123"

echo
echo "== 0. La API responde =="
check "GET /v3/api-docs" 200 "$(code "$BASE/v3/api-docs")"
check "Swagger UI disponible" 200 "$(code -L "$BASE/swagger-ui/index.html")"

echo
echo "== 1. Lecturas publicas (sin token) =="
for ruta in asignaturas entidades profesores semestres cursos alumnos inscripciones; do
  check "GET /$ruta" 200 "$(code "$BASE/$ruta")"
done
check "GET /cursos/999999 (no existe)" 404 "$(code "$BASE/cursos/999999")"
check "GET /alumnos/999999 (no existe)" 404 "$(code "$BASE/alumnos/999999")"
check "GET /profesores/999999 (no existe)" 404 "$(code "$BASE/profesores/999999")"
check "GET /semestres/999999 (no existe)" 404 "$(code "$BASE/semestres/999999")"

echo
echo "== 2. Escritura sin token debe ser rechazada =="
check "POST /asignaturas sin token" 401 "$(code -X POST "$BASE/asignaturas" -H "$JSON" -d '{}')"
check "POST /alumnos sin token" 401 "$(code -X POST "$BASE/alumnos" -H "$JSON" -d '{}')"
check "POST /inscripciones sin token" 401 "$(code -X POST "$BASE/inscripciones" -H "$JSON" -d '{}')"
check "POST /asignaturas con token falso" 401 "$(code -X POST "$BASE/asignaturas" -H "$JSON" -H 'Authorization: Bearer abc.def.ghi' -d '{}')"

echo
echo "== 3. Registro e inicio de sesion =="
check "Registrar usuario nuevo" 201 "$(code -X POST "$BASE/auth/register" -H "$JSON" -d "{\"username\":\"$USUARIO\",\"password\":\"$CLAVE_USER\"}")"
check "Registrar el mismo usuario otra vez" 409 "$(code -X POST "$BASE/auth/register" -H "$JSON" -d "{\"username\":\"$USUARIO\",\"password\":\"$CLAVE_USER\"}")"
check "Registrar con contrasena corta" 400 "$(code -X POST "$BASE/auth/register" -H "$JSON" -d '{"username":"otro","password":"123"}')"
check "Login con contrasena incorrecta" 401 "$(code -X POST "$BASE/auth/login" -H "$JSON" -d "{\"username\":\"$USUARIO\",\"password\":\"incorrecta\"}")"

TOKEN_USER=$(token "$USUARIO" "$CLAVE_USER")
TOKEN_ADMIN=$(token "admin" "$ADMIN_PASS")
[ -n "$TOKEN_USER" ] && [ "${#TOKEN_USER}" -gt 20 ] && check "Login de usuario devuelve token" ok ok || check "Login de usuario devuelve token" ok vacio
[ -n "$TOKEN_ADMIN" ] && [ "${#TOKEN_ADMIN}" -gt 20 ] && check "Login de admin devuelve token" ok ok || check "Login de admin devuelve token (revisa DEMO_ADMIN_PASSWORD)" ok vacio

if [ "${#TOKEN_ADMIN}" -le 20 ] || [ "${#TOKEN_USER}" -le 20 ]; then
  echo
  echo "No se pudo obtener algun token; se omiten las pruebas siguientes."
  echo "Revisa que la API este arrancada y que DEMO_ADMIN_PASSWORD coincida con la usada al arrancarla."
  exit 1
fi

AUTH_USER="Authorization: Bearer $TOKEN_USER"
AUTH_ADMIN="Authorization: Bearer $TOKEN_ADMIN"

echo
echo "== 4. Un usuario normal (USER) NO puede modificar catalogos =="
check "POST /asignaturas con USER" 403 "$(code -X POST "$BASE/asignaturas" -H "$JSON" -H "$AUTH_USER" -d '{}')"
check "POST /entidades con USER" 403 "$(code -X POST "$BASE/entidades" -H "$JSON" -H "$AUTH_USER" -d '{}')"
check "POST /profesores con USER" 403 "$(code -X POST "$BASE/profesores" -H "$JSON" -H "$AUTH_USER" -d '{}')"
check "POST /semestres con USER" 403 "$(code -X POST "$BASE/semestres" -H "$JSON" -H "$AUTH_USER" -d '{}')"
check "POST /cursos con USER" 403 "$(code -X POST "$BASE/cursos" -H "$JSON" -H "$AUTH_USER" -d '{}')"
check "POST /alumnos con USER" 403 "$(code -X POST "$BASE/alumnos" -H "$JSON" -H "$AUTH_USER" -d '{}')"

echo
echo "== 5. El administrador crea los datos (catalogos y oferta) =="
ABREV=$(printf "%03d" $((RANDOM % 1000)))
INEGI=$(printf "%02d" $((RANDOM % 100)))
RESP=$(body -X POST "$BASE/entidades" -H "$JSON" -H "$AUTH_ADMIN" \
  -d "{\"nombre\":\"Estado$R\",\"abreviatura\":\"$ABREV\",\"capital\":\"Capital\",\"region\":\"Centro\",\"claveInegi\":\"$INEGI\"}")
ID_ENTIDAD=$(campo "$RESP" idEntidad)
check "Crear entidad federativa" "numero" "$([[ "$ID_ENTIDAD" =~ ^[0-9]+$ ]] && echo numero || echo "no-numero")"

RESP=$(body -X POST "$BASE/asignaturas" -H "$JSON" -H "$AUTH_ADMIN" \
  -d "{\"clave\":\"A$((RANDOM % 9000 + 1000))\",\"nombre\":\"Bases de datos $R\",\"horaTeorica\":3,\"horaPractica\":2,\"creditos\":8}")
ID_ASIG=$(campo "$RESP" idAsignatura)
check "Crear asignatura" "numero" "$([[ "$ID_ASIG" =~ ^[0-9]+$ ]] && echo numero || echo "no-numero")"

RESP=$(body -X POST "$BASE/profesores" -H "$JSON" -H "$AUTH_ADMIN" \
  -d "{\"numeroEmpleado\":\"E$R\",\"nombre\":\"Ana\",\"apellidoPaterno\":\"Lopez\",\"correo\":\"prof$R@fca.unam.mx\",\"fechaNacimiento\":\"1980-05-20\"}")
ID_PROF=$(campo "$RESP" idProfesor)
check "Crear profesor" "numero" "$([[ "$ID_PROF" =~ ^[0-9]+$ ]] && echo numero || echo "no-numero")"

RESP=$(body -X POST "$BASE/semestres" -H "$JSON" -H "$AUTH_ADMIN" \
  -d "{\"clave\":\"S$R\",\"fechaInicio\":\"2026-08-03\",\"fechaFin\":\"2026-12-04\",\"anio\":2026}")
ID_SEM=$(campo "$RESP" idSemestre)
check "Crear semestre" "numero" "$([[ "$ID_SEM" =~ ^[0-9]+$ ]] && echo numero || echo "no-numero")"

CLAVE_CURSO="C$R"
CUERPO_CURSO="{\"clave\":\"$CLAVE_CURSO\",\"cupo\":1,\"turno\":\"matutino\",\"idAsignatura\":$ID_ASIG,\"idProfesor\":$ID_PROF,\"idSemestre\":$ID_SEM}"
RESP=$(body -X POST "$BASE/cursos" -H "$JSON" -H "$AUTH_ADMIN" -d "$CUERPO_CURSO")
ID_CURSO=$(campo "$RESP" idCurso)
check "Crear curso (cupo 1)" "numero" "$([[ "$ID_CURSO" =~ ^[0-9]+$ ]] && echo numero || echo "no-numero")"
check "Curso repetido en el mismo semestre" 409 "$(code -X POST "$BASE/cursos" -H "$JSON" -H "$AUTH_ADMIN" -d "$CUERPO_CURSO")"
check "Curso con asignatura inexistente" 400 "$(code -X POST "$BASE/cursos" -H "$JSON" -H "$AUTH_ADMIN" -d "{\"clave\":\"X$R\",\"cupo\":5,\"turno\":\"matutino\",\"idAsignatura\":999999,\"idSemestre\":$ID_SEM}")"

cuerpo_alumno() {
  echo "{\"numeroCuenta\":\"$1\",\"nombre\":\"$2\",\"apellidoPaterno\":\"Perez\",\"fechaNacimiento\":\"2003-01-15\",\"correo\":\"$3\",\"generacion\":2024,\"idEntidad\":$4}"
}
CUENTA1=$(printf "%09d" $(( (RANDOM * 30000 + RANDOM) % 1000000000 )))
CUENTA2=$(printf "%09d" $(( (RANDOM * 30000 + RANDOM) % 1000000000 )))
RESP=$(body -X POST "$BASE/alumnos" -H "$JSON" -H "$AUTH_ADMIN" -d "$(cuerpo_alumno "$CUENTA1" Luis "luis$R@correo.com" "$ID_ENTIDAD")")
ID_AL1=$(campo "$RESP" idAlumno)
check "Crear alumno 1" "numero" "$([[ "$ID_AL1" =~ ^[0-9]+$ ]] && echo numero || echo "no-numero")"
RESP=$(body -X POST "$BASE/alumnos" -H "$JSON" -H "$AUTH_ADMIN" -d "$(cuerpo_alumno "$CUENTA2" Maria "maria$R@correo.com" "$ID_ENTIDAD")")
ID_AL2=$(campo "$RESP" idAlumno)
check "Crear alumno 2" "numero" "$([[ "$ID_AL2" =~ ^[0-9]+$ ]] && echo numero || echo "no-numero")"
check "Alumno con entidad inexistente" 400 "$(code -X POST "$BASE/alumnos" -H "$JSON" -H "$AUTH_ADMIN" -d "$(cuerpo_alumno 123456789 Test "t$R@correo.com" 999999)")"
check "Alumno con correo invalido" 400 "$(code -X POST "$BASE/alumnos" -H "$JSON" -H "$AUTH_ADMIN" -d "$(cuerpo_alumno 123456780 Test "esto-no-es-correo" "$ID_ENTIDAD")")"
check "Alumno repetido (mismo numero de cuenta)" 409 "$(code -X POST "$BASE/alumnos" -H "$JSON" -H "$AUTH_ADMIN" -d "$(cuerpo_alumno "$CUENTA1" Luis "otro$R@correo.com" "$ID_ENTIDAD")")"

echo
echo "== 6. Relacion muchos a muchos: inscripciones =="
check "Inscribir alumno 1 al curso (USER)" 201 "$(code -X POST "$BASE/inscripciones" -H "$JSON" -H "$AUTH_USER" -d "{\"idAlumno\":$ID_AL1,\"idCurso\":$ID_CURSO}")"
check "Repetir la misma inscripcion" 409 "$(code -X POST "$BASE/inscripciones" -H "$JSON" -H "$AUTH_USER" -d "{\"idAlumno\":$ID_AL1,\"idCurso\":$ID_CURSO}")"
check "Inscribir alumno 2 (curso sin cupo)" 409 "$(code -X POST "$BASE/inscripciones" -H "$JSON" -H "$AUTH_USER" -d "{\"idAlumno\":$ID_AL2,\"idCurso\":$ID_CURSO}")"
check "Inscripcion sin idAlumno" 400 "$(code -X POST "$BASE/inscripciones" -H "$JSON" -H "$AUTH_USER" -d "{\"idCurso\":$ID_CURSO}")"
check "Inscripcion con alumno inexistente" 404 "$(code -X POST "$BASE/inscripciones" -H "$JSON" -H "$AUTH_USER" -d "{\"idAlumno\":999999,\"idCurso\":$ID_CURSO}")"
check "GET /alumnos/$ID_AL1/inscripciones" 200 "$(code "$BASE/alumnos/$ID_AL1/inscripciones")"
check "GET /cursos/$ID_CURSO/inscripciones" 200 "$(code "$BASE/cursos/$ID_CURSO/inscripciones")"
check "GET inscripciones de alumno inexistente" 404 "$(code "$BASE/alumnos/999999/inscripciones")"
LISTA=$(body "$BASE/cursos/$ID_CURSO/inscripciones")
check "El curso lista al alumno inscrito" "si" "$(echo "$LISTA" | grep -q "\"idAlumno\":$ID_AL1" && echo si || echo no)"
check "Dar de baja la inscripcion" 204 "$(code -X DELETE "$BASE/inscripciones/$ID_AL1/$ID_CURSO/ordinaria" -H "$AUTH_USER")"
check "Dar de baja otra vez" 404 "$(code -X DELETE "$BASE/inscripciones/$ID_AL1/$ID_CURSO/ordinaria" -H "$AUTH_USER")"
check "Baja sin token" 401 "$(code -X DELETE "$BASE/inscripciones/$ID_AL1/$ID_CURSO/ordinaria")"
check "Tras la baja, alumno 2 si puede inscribirse" 201 "$(code -X POST "$BASE/inscripciones" -H "$JSON" -H "$AUTH_USER" -d "{\"idAlumno\":$ID_AL2,\"idCurso\":$ID_CURSO}")"

echo
echo "============================================================"
echo "  RESULTADO: $ok pruebas correctas, $fallas con falla"
echo "============================================================"
[ "$fallas" -eq 0 ] && exit 0 || exit 1
