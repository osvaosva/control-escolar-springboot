package mx.unam.fca.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "semestre")
public class Semestre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_semestre")
    private Integer idSemestre;

    @NotBlank
    @Size(max = 6)
    @Column(nullable = false, unique = true, length = 6)
    private String clave;

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @NotNull
    @Min(2000)
    @Column(nullable = false)
    private Integer anio;

    @Pattern(regexp = "activo|cerrado")
    @Column(nullable = false, length = 10)
    private String estatus = "activo";

    public Semestre() { }

    public Integer getIdSemestre() { return idSemestre; }
    public void setIdSemestre(Integer idSemestre) { this.idSemestre = idSemestre; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }
}