package mx.unam.fca.demo.dto;

import java.time.LocalDate;
import mx.unam.fca.demo.models.Alumno;

public record AlumnoResponse(
        Integer idAlumno, String numeroCuenta, String nombre, String apellidoPaterno,
        String apellidoMaterno, LocalDate fechaNacimiento, String correo, Integer generacion,
        String telefono, LocalDate fechaAlta, LocalDate fechaBaja, String estatus,
        Integer idEntidad, String entidad) {

    public static AlumnoResponse from(Alumno a) {
        return new AlumnoResponse(
                a.getIdAlumno(), a.getNumeroCuenta(), a.getNombre(), a.getApellidoPaterno(),
                a.getApellidoMaterno(), a.getFechaNacimiento(), a.getCorreo(), a.getGeneracion(),
                a.getTelefono(), a.getFechaAlta(), a.getFechaBaja(), a.getEstatus(),
                a.getEntidad().getIdEntidad(), a.getEntidad().getNombre());
    }
}
