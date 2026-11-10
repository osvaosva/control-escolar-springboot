package mx.unam.fca.demo.repositories;

import mx.unam.fca.demo.models.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {
    boolean existsByNumeroCuenta(String numeroCuenta);
    boolean existsByCorreo(String correo);
}
