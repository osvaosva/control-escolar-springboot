package mx.unam.fca.demo.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "inscripcion")
public class Inscripcion {

    @EmbeddedId
    private InscripcionId id;

    @ManyToOne(optional = false)
    @MapsId("idAlumno")
    @JoinColumn(name = "id_alumno")
    private Alumno alumno;

    @ManyToOne(optional = false)
    @MapsId("idCurso")
    @JoinColumn(name = "id_curso")
    private Curso curso;

    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDate fechaInscripcion = LocalDate.now();

    @Column(precision = 4, scale = 1)
    private BigDecimal calificacion;

    @Column(nullable = false, length = 12)
    private String estatus = "inscrito";

    @Column(name = "porcentaje_asistencia", nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentajeAsistencia = new BigDecimal("100.00");

    @Column(name = "modo_inscripcion", nullable = false, length = 12)
    private String modoInscripcion = "presencial";

    public Inscripcion() { }

    public InscripcionId getId() { return id; }
    public void setId(InscripcionId id) { this.id = id; }

    public Alumno getAlumno() { return alumno; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    public LocalDate getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(LocalDate fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }

    public BigDecimal getCalificacion() { return calificacion; }
    public void setCalificacion(BigDecimal calificacion) { this.calificacion = calificacion; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public BigDecimal getPorcentajeAsistencia() { return porcentajeAsistencia; }
    public void setPorcentajeAsistencia(BigDecimal porcentajeAsistencia) { this.porcentajeAsistencia = porcentajeAsistencia; }

    public String getModoInscripcion() { return modoInscripcion; }
    public void setModoInscripcion(String modoInscripcion) { this.modoInscripcion = modoInscripcion; }
}