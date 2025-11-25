package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record PeliculaResponseDTO(
        Long id,
        String titulo,
        String genero,
        LocalDate fechaEstreno,
        DirectorSimpleDTO director,
        Set<ActorSimpleDTO> reparto
) {
}
