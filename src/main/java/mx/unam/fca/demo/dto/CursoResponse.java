package mx.unam.fca.demo.dto;

import mx.unam.fca.demo.models.Curso;
import mx.unam.fca.demo.models.Profesor;

public record CursoResponse(
        Integer idCurso,
        String clave,
        Integer cupo,
        String modalidad,
        String idioma,
        String turno,
        Long idAsignatura,
        String asignatura,
        Integer idProfesor,
        String profesor,
        Integer idSemestre,
        String semestre) {

    public static CursoResponse from(Curso c) {
        Profesor p = c.getProfesor();
        return new CursoResponse(
                c.getIdCurso(),
                c.getClave(),
                c.getCupo(),
                c.getModalidad(),
                c.getIdioma(),
                c.getTurno(),
                c.getAsignatura().getIdAsignatura(),
                c.getAsignatura().getNombre(),
                p == null ? null : p.getIdProfesor(),
                p == null ? null : p.getNombre() + " " + p.getApellidoPaterno(),
                c.getSemestre().getIdSemestre(),
                c.getSemestre().getClave());
    }
}