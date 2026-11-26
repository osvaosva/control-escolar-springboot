package mx.unam.fca.demo.repositories;
import mx.unam.fca.demo.models.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CursoRepository extends JpaRepository<Curso, Integer> {
boolean existsByClaveAndSemestre_IdSemestre(String clave, Integer
idSemestre);
}
