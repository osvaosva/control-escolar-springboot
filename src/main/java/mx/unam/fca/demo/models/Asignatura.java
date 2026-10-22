package mx.unam.fca.demo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "asignatura")
public class Asignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignatura")
    private Long idAsignatura;

    @Column(nullable = false, unique = true, length = 5)
    private String clave;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "hora_teorica", nullable = false)
    private Integer horaTeorica;

    @Column(name = "hora_practica", nullable = false)
    private Integer horaPractica;

    @Column(nullable = false)
    private Integer creditos;

    public Asignatura() { }

    // Getters y setters
    public Long getIdAsignatura() { return idAsignatura; }
    public void setIdAsignatura(Long idAsignatura) { this.idAsignatura = idAsignatura; }
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getHoraTeorica() { return horaTeorica; }
    public void setHoraTeorica(Integer horaTeorica) { this.horaTeorica = horaTeorica; }
    public Integer getHoraPractica() { return horaPractica; }
    public void setHoraPractica(Integer horaPractica) { this.horaPractica = horaPractica; }
    public Integer getCreditos() { return creditos; }
    public void setCreditos(Integer creditos) { this.creditos = creditos; }
}