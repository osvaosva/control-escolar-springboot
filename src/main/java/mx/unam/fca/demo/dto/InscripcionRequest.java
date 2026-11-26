package mx.unam.fca.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record InscripcionRequest(
        @NotNull Integer idAlumno,
        @NotNull Integer idCurso,
        @Pattern(regexp = "ordinaria|extraordinaria|especial") String tipoInscripcion,
        @Pattern(regexp = "presencial|en_linea") String modoInscripcion) {
}