package mx.unam.fca.demo.controllers;

import mx.unam.fca.demo.models.Asignatura;
import mx.unam.fca.demo.repositories.AsignaturaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asignaturas")
public class AsignaturaController {

    private final AsignaturaRepository repository;

    public AsignaturaController(AsignaturaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Asignatura> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Asignatura obtener(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Asignatura crear(@RequestBody Asignatura asignatura) {
        return repository.save(asignatura);
    }
}
