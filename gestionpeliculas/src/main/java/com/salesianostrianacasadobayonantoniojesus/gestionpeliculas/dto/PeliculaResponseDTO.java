package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto;

import java.time.LocalDate;
import java.util.List;

public record PeliculaResponseDTO(
        Long id,
        String titulo,
        String genero,
        LocalDate fechaEstreno,
        DirectorSimpleDTO director,
        List<ActorSimpleDTO> reparto
) {
}
