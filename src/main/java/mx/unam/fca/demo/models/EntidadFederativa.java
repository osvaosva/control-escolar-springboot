package mx.unam.fca.demo.models;
import jakarta.persistence.*;
@Entity 
@Table(name = "entidad_federativa")
public class EntidadFederativa {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entidad")
    private Integer idEntidad;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(nullable = false, unique = true, length = 3)
    private String abreviatura;

    @Column(nullable = false, length = 50)
    private String capital;

    @Column(nullable = false, length = 30)
    private String region;

    @Column(name = "clave_inegi", nullable = false, unique = true, length = 2)
    private String claveInegi;

    public Integer getIdEntidad() { return idEntidad; }
    public void setIdEntidad(Integer idEntidad) { this.idEntidad = idEntidad; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getAbreviatura() { return abreviatura; }
    public void setAbreviatura(String abreviatura) { this.abreviatura = abreviatura; }

    public String getCapital() { return capital; }
    public void setCapital(String capital) { this.capital = capital; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getClaveInegi() { return claveInegi; }
    public void setClaveInegi(String claveInegi) { this.claveInegi = claveInegi; }




}