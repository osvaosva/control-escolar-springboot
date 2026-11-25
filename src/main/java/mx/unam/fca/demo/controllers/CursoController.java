package mx.unam.fca.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import mx.unam.fca.demo.dto.CursoRequest;
import mx.unam.fca.demo.dto.CursoResponse;
import mx.unam.fca.demo.models.*;
import mx.unam.fca.demo.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/cursos")
@Tag(name = "Cursos", description = "Oferta de asignaturas por semestre")
public class CursoController {

    private final CursoRepository cursos;
    private final AsignaturaRepository asignaturas;
    private final ProfesorRepository profesores;
    private final SemestreRepository semestres;

    public CursoController(CursoRepository cursos, AsignaturaRepository asignaturas,
            ProfesorRepository profesores, SemestreRepository semestres) {
        this.cursos = cursos;
        this.asignaturas = asignaturas;
        this.profesores = profesores;
        this.semestres = semestres;
    }

    @GetMapping
    @Operation(summary = "Listar cursos")
    public List<CursoResponse> listar() {
        return cursos.findAll().stream().map(CursoResponse::from).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un curso por ID")
    public ResponseEntity<CursoResponse> obtener(@PathVariable Integer id) {
        return cursos.findById(id)
                .map(c -> ResponseEntity.ok(CursoResponse.from(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un curso")
    public ResponseEntity<CursoResponse> crear(@Valid @RequestBody CursoRequest req) {
        Asignatura asignatura = asignaturas.findById(req.idAsignatura())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "La asignatura no existe"));

        Semestre semestre = semestres.findById(req.idSemestre())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El semestre no existe"));

        Profesor profesor = null;
        if (req.idProfesor() != null) {
            profesor = profesores.findById(req.idProfesor())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El profesor no existe"));
        }

        if (cursos.existsByClaveAndSemestre_IdSemestre(req.clave(), req.idSemestre())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un curso con esa clave en ese semestre");
        }

        Curso curso = new Curso();
        curso.setClave(req.clave());
        curso.setCupo(req.cupo());
        if (req.modalidad() != null) curso.setModalidad(req.modalidad());
        if (req.idioma() != null) curso.setIdioma(req.idioma());
        curso.setTurno(req.turno());
        curso.setAsignatura(asignatura);
        curso.setProfesor(profesor);
        curso.setSemestre(semestre);

        Curso guardado = cursos.save(curso);
        return ResponseEntity.created(URI.create("/cursos/" + guardado.getIdCurso()))
                .body(CursoResponse.from(guardado));
    }
}