package mx.unam.fca.demo.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record AlumnoRequest(
        @NotBlank @Size(max = 9) String numeroCuenta,
        @NotBlank @Size(max = 50) String nombre,
        @NotBlank @Size(max = 50) String apellidoPaterno,
        @Size(max = 50) String apellidoMaterno,
        @NotNull @Past LocalDate fechaNacimiento,
        @NotBlank @Email @Size(max = 80) String correo,
        @NotNull @Min(2000) Integer generacion,
        @Size(max = 15) String telefono,
        @NotNull Integer idEntidad) {
}
