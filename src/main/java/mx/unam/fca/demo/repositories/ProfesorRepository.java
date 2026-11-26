package mx.unam.fca.demo.repositories;

import mx.unam.fca.demo.models.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfesorRepository extends JpaRepository<Profesor, Integer> {
    boolean existsByNumeroEmpleado(String numeroEmpleado);
    boolean existsByCorreo(String correo);
}