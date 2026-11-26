package mx.unam.fca.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import mx.unam.fca.demo.models.Asignatura;
import mx.unam.fca.demo.repositories.AsignaturaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/asignaturas")
@Tag(name = "Asignaturas", description = "Catálogo de materias")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AsignaturaController {

    private final AsignaturaRepository repository;

    public AsignaturaController(AsignaturaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    @SecurityRequirements
    @Operation(summary = "Listar asignaturas")
    public List<Asignatura> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @SecurityRequirements
    @Operation(summary = "Buscar una asignatura por ID")
    public Asignatura obtener(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    @Operation(summary = "Crear una asignatura (solo ADMIN)")
    public Asignatura crear(@RequestBody Asignatura asignatura) {
        return repository.save(asignatura);
    }
}