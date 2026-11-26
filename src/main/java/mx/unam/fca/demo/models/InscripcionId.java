package mx.unam.fca.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class InscripcionId implements Serializable {

    @Column(name = "id_alumno")
    private Integer idAlumno;

    @Column(name = "id_curso")
    private Integer idCurso;

    @Column(name = "tipo_inscripcion", length = 12)
    private String tipoInscripcion;

    public InscripcionId() { }

    public InscripcionId(Integer idAlumno, Integer idCurso, String tipoInscripcion) {
        this.idAlumno = idAlumno;
        this.idCurso = idCurso;
        this.tipoInscripcion = tipoInscripcion;
    }

    public Integer getIdAlumno() { return idAlumno; }
    public Integer getIdCurso() { return idCurso; }
    public String getTipoInscripcion() { return tipoInscripcion; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InscripcionId other)) return false;
        return Objects.equals(idAlumno, other.idAlumno)
                && Objects.equals(idCurso, other.idCurso)
                && Objects.equals(tipoInscripcion, other.tipoInscripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAlumno, idCurso, tipoInscripcion);
    }
}