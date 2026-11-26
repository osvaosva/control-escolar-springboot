package mx.unam.fca.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import mx.unam.fca.demo.models.EntidadFederativa;
import mx.unam.fca.demo.repositories.EntidadFederativaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entidades")
@Tag(name = "Entidades federativas", description = "Catálogo de estados")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class EntidadFederativaController {

    private final EntidadFederativaRepository repository;

    public EntidadFederativaController(EntidadFederativaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    @SecurityRequirements
    @Operation(summary = "Listar entidades federativas")
    public List<EntidadFederativa> obtenerTodas() {
        return repository.findAll();
    }

    @PostMapping
    @Operation(summary = "Crear una entidad federativa (solo ADMIN)")
    public EntidadFederativa crear(@RequestBody EntidadFederativa entidad) {
        return repository.save(entidad);
    }
}