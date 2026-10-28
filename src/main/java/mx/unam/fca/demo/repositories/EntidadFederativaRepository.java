package mx.unam.fca.demo.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import mx.unam.fca.demo.models.EntidadFederativa;

public interface EntidadFederativaRepository extends JpaRepository<EntidadFederativa, Integer> {
    // Puedes agregar métodos de consulta personalizados si es necesario
}