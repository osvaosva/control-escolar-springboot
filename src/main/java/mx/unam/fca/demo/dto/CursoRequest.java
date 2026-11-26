package mx.unam.fca.demo.dto;
import jakarta.validation.constraints.*;
public record CursoRequest(
@NotBlank @Size(max = 6) String clave,
@NotNull @Positive Integer cupo,
@Pattern(regexp = "presencial|virtual|mixta") String modalidad,
@Size(max = 20) String idioma,
@NotBlank @Pattern(regexp = "matutino|vespertino|nocturno") String
turno,
@NotNull Long idAsignatura,
Integer idProfesor,
@NotNull Integer idSemestre) {
}