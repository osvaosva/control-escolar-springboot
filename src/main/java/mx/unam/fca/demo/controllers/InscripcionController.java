package mx.unam.fca.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import mx.unam.fca.demo.dto.InscripcionRequest;
import mx.unam.fca.demo.dto.InscripcionResponse;
import mx.unam.fca.demo.models.Alumno;
import mx.unam.fca.demo.models.Curso;
import mx.unam.fca.demo.models.Inscripcion;
import mx.unam.fca.demo.models.InscripcionId;
import mx.unam.fca.demo.repositories.AlumnoRepository;
import mx.unam.fca.demo.repositories.CursoRepository;
import mx.unam.fca.demo.repositories.InscripcionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Tag(name = "Inscripciones", description = "Relación muchos a muchos entre alumnos y cursos")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class InscripcionController {

    private final InscripcionRepository inscripciones;
    private final AlumnoRepository alumnos;
    private final CursoRepository cursos;

    public InscripcionController(InscripcionRepository inscripciones,
            AlumnoRepository alumnos, CursoRepository cursos) {
        this.inscripciones = inscripciones;
        this.alumnos = alumnos;
        this.cursos = cursos;
    }

    @GetMapping("/inscripciones")
    @PreAuthorize("permitAll()")
    @SecurityRequirements
    @Operation(summary = "Listar todas las inscripciones")
    public List<InscripcionResponse> listar() {
        return inscripciones.findAll().stream().map(InscripcionResponse::from).toList();
    }

    @GetMapping("/alumnos/{idAlumno}/inscripciones")
    @PreAuthorize("permitAll()")
    @SecurityRequirements
    @Operation(summary = "Cursos en los que está inscrito un alumno")
    public ResponseEntity<List<InscripcionResponse>> deAlumno(@PathVariable Integer idAlumno) {
        if (!alumnos.existsById(idAlumno)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inscripciones.findByAlumno_IdAlumno(idAlumno).stream()
                .map(InscripcionResponse::from).toList());
    }

    @GetMapping("/cursos/{idCurso}/inscripciones")
    @PreAuthorize("permitAll()")
    @SecurityRequirements
    @Operation(summary = "Alumnos inscritos en un curso")
    public ResponseEntity<List<InscripcionResponse>> deCurso(@PathVariable Integer idCurso) {
        if (!cursos.existsById(idCurso)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inscripciones.findByCurso_IdCurso(idCurso).stream()
                .map(InscripcionResponse::from).toList());
    }

    @PostMapping("/inscripciones")
    @Operation(summary = "Inscribir un alumno a un curso (valida cupo y duplicados)")
    public ResponseEntity<InscripcionResponse> inscribir(@Valid @RequestBody InscripcionRequest req) {
        Alumno alumno = alumnos.findById(req.idAlumno())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El alumno no existe"));
        Curso curso = cursos.findById(req.idCurso())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El curso no existe"));

        String tipo = req.tipoInscripcion() == null ? "ordinaria" : req.tipoInscripcion();
        InscripcionId id = new InscripcionId(alumno.getIdAlumno(), curso.getIdCurso(), tipo);

        if (inscripciones.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El alumno ya está inscrito en ese curso con ese tipo");
        }
        if (inscripciones.countByCurso_IdCurso(curso.getIdCurso()) >= curso.getCupo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El curso no tiene cupo disponible");
        }

        Inscripcion nueva = new Inscripcion();
        nueva.setId(id);
        nueva.setAlumno(alumno);
        nueva.setCurso(curso);
        if (req.modoInscripcion() != null) {
            nueva.setModoInscripcion(req.modoInscripcion());
        }

        Inscripcion guardada = inscripciones.save(nueva);
        URI location = URI.create("/inscripciones/" + id.getIdAlumno() + "/" + id.getIdCurso() + "/" + tipo);
        return ResponseEntity.created(location).body(InscripcionResponse.from(guardada));
    }

    @DeleteMapping("/inscripciones/{idAlumno}/{idCurso}/{tipo}")
    @Operation(summary = "Dar de baja una inscripción")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idAlumno,
            @PathVariable Integer idCurso, @PathVariable String tipo) {
        InscripcionId id = new InscripcionId(idAlumno, idCurso, tipo);
        if (!inscripciones.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        inscripciones.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}