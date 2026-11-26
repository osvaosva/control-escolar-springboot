package mx.unam.fca.demo.repositories;

import java.util.List;
import mx.unam.fca.demo.models.Inscripcion;
import mx.unam.fca.demo.models.InscripcionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionRepository extends JpaRepository<Inscripcion, InscripcionId> {

    List<Inscripcion> findByAlumno_IdAlumno(Integer idAlumno);

    List<Inscripcion> findByCurso_IdCurso(Integer idCurso);

    long countByCurso_IdCurso(Integer idCurso);
}
