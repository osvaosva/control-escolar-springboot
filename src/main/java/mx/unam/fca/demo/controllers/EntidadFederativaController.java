package mx.unam.fca.demo.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import mx.unam.fca.demo.models.EntidadFederativa;
import mx.unam.fca.demo.repositories.EntidadFederativaRepository;

@RestController 
@RequestMapping ("/entidades")
public class EntidadFederativaController {

    private final EntidadFederativaRepository repository;

    public EntidadFederativaController(EntidadFederativaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<EntidadFederativa> obtenerTodas(){
        return repository.findAll();
    }

    @PostMapping
    public EntidadFederativa crear (@RequestBody EntidadFederativa entidad){
        return repository.save(entidad);
    }
}





 
