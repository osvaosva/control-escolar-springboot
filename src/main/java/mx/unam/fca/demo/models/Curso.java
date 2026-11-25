package mx.unam.fca.demo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "curso",
        uniqueConstraints = @UniqueConstraint(columnNames = {"clave", "id_semestre"}))
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Integer idCurso;

    @Column(nullable = false, length = 6)
    private String clave;

    @Column(nullable = false)
    private Integer cupo;

    @Column(nullable = false, length = 12)
    private String modalidad = "presencial";

    @Column(nullable = false, length = 20)
    private String idioma = "Español";

    @Column(nullable = false, length = 12)
    private String turno;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_asignatura", nullable = false)
    private Asignatura asignatura;

    @ManyToOne
    @JoinColumn(name = "id_profesor")
    private Profesor profesor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_semestre", nullable = false)
    private Semestre semestre;

    public Curso() { }

    public Integer getIdCurso() { return idCurso; }
    public void setIdCurso(Integer idCurso) { this.idCurso = idCurso; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public Integer getCupo() { return cupo; }
    public void setCupo(Integer cupo) { this.cupo = cupo; }

    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }

    public Asignatura getAsignatura() { return asignatura; }
    public void setAsignatura(Asignatura asignatura) { this.asignatura = asignatura; }

    public Profesor getProfesor() { return profesor; }
    public void setProfesor(Profesor profesor) { this.profesor = profesor; }

    public Semestre getSemestre() { return semestre; }
    public void setSemestre(Semestre semestre) { this.semestre = semestre; }
}