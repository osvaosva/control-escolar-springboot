package mx.unam.fca.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import mx.unam.fca.demo.models.Semestre;
import mx.unam.fca.demo.repositories.SemestreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/semestres")
@Tag(name = "Semestres", description = "Periodos académicos")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class SemestreController {

    private final SemestreRepository repository;

    public SemestreController(SemestreRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    @SecurityRequirements
    @Operation(summary = "Listar semestres")
    public List<Semestre> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @SecurityRequirements
    @Operation(summary = "Buscar un semestre por ID")
    public ResponseEntity<Semestre> obtener(@PathVariable Integer id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un semestre (solo ADMIN)")
    public ResponseEntity<Semestre> crear(@Valid @RequestBody Semestre semestre) {
        semestre.setIdSemestre(null);
        Semestre guardado = repository.save(semestre);
        return ResponseEntity.created(URI.create("/semestres/" + guardado.getIdSemestre()))
                .body(guardado);
    }
}