package mx.unam.fca.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import mx.unam.fca.demo.models.Profesor;
import mx.unam.fca.demo.repositories.ProfesorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/profesores")
@Tag(name = "Profesores", description = "Plantilla docente")
public class ProfesorController {

    private final ProfesorRepository repository;

    public ProfesorController(ProfesorRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(summary = "Listar profesores")
    public List<Profesor> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un profesor por ID")
    public ResponseEntity<Profesor> obtener(@PathVariable Integer id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un profesor")
    public ResponseEntity<Profesor> crear(@Valid @RequestBody Profesor profesor) {
        if (repository.existsByNumeroEmpleado(profesor.getNumeroEmpleado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un profesor con ese número de empleado");
        }
        if (repository.existsByCorreo(profesor.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un profesor con ese correo");
        }
        profesor.setIdProfesor(null);
        Profesor guardado = repository.save(profesor);
        return ResponseEntity.created(URI.create("/profesores/" + guardado.getIdProfesor()))
                .body(guardado);
    }
}