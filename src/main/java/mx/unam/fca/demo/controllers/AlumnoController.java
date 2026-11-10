package mx.unam.fca.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import mx.unam.fca.demo.dto.AlumnoRequest;
import mx.unam.fca.demo.dto.AlumnoResponse;
import mx.unam.fca.demo.models.Alumno;
import mx.unam.fca.demo.models.EntidadFederativa;
import mx.unam.fca.demo.repositories.AlumnoRepository;
import mx.unam.fca.demo.repositories.EntidadFederativaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/alumnos")
@Tag(name = "Alumnos", description = "Matrícula de estudiantes")
public class AlumnoController {

    private final AlumnoRepository alumnos;
    private final EntidadFederativaRepository entidades;

    public AlumnoController(AlumnoRepository alumnos, EntidadFederativaRepository entidades) {
        this.alumnos = alumnos;
        this.entidades = entidades;
    }

    @GetMapping
    @Operation(summary = "Listar alumnos")
    public List<AlumnoResponse> listar() {
        return alumnos.findAll().stream().map(AlumnoResponse::from).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un alumno por ID")
    public ResponseEntity<AlumnoResponse> obtener(@PathVariable Integer id) {
        return alumnos.findById(id)
                .map(a -> ResponseEntity.ok(AlumnoResponse.from(a)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Registrar un alumno")
    public ResponseEntity<AlumnoResponse> crear(@Valid @RequestBody AlumnoRequest req) {
        EntidadFederativa entidad = entidades.findById(req.idEntidad())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "La entidad federativa no existe"));
        if (alumnos.existsByNumeroCuenta(req.numeroCuenta())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un alumno con ese número de cuenta");
        }
        if (alumnos.existsByCorreo(req.correo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un alumno con ese correo");
        }

        Alumno alumno = new Alumno();
        alumno.setNumeroCuenta(req.numeroCuenta());
        alumno.setNombre(req.nombre());
        alumno.setApellidoPaterno(req.apellidoPaterno());
        alumno.setApellidoMaterno(req.apellidoMaterno());
        alumno.setFechaNacimiento(req.fechaNacimiento());
        alumno.setCorreo(req.correo());
        alumno.setGeneracion(req.generacion());
        alumno.setTelefono(req.telefono());
        alumno.setEntidad(entidad);

        Alumno guardado = alumnos.save(alumno);
        return ResponseEntity.created(URI.create("/alumnos/" + guardado.getIdAlumno()))
                .body(AlumnoResponse.from(guardado));
    }
}
