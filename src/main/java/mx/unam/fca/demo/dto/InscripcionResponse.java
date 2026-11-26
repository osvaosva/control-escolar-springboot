package mx.unam.fca.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import mx.unam.fca.demo.models.Inscripcion;

public record InscripcionResponse(
        Integer idAlumno,
        String alumno,
        String numeroCuenta,
        Integer idCurso,
        String claveCurso,
        String tipoInscripcion,
        LocalDate fechaInscripcion,
        BigDecimal calificacion,
        String estatus,
        BigDecimal porcentajeAsistencia,
        String modoInscripcion) {

    public static InscripcionResponse from(Inscripcion i) {
        return new InscripcionResponse(
                i.getAlumno().getIdAlumno(),
                i.getAlumno().getNombre() + " " + i.getAlumno().getApellidoPaterno(),
                i.getAlumno().getNumeroCuenta(),
                i.getCurso().getIdCurso(),
                i.getCurso().getClave(),
                i.getId().getTipoInscripcion(),
                i.getFechaInscripcion(),
                i.getCalificacion(),
                i.getEstatus(),
                i.getPorcentajeAsistencia(),
                i.getModoInscripcion());
    }
}