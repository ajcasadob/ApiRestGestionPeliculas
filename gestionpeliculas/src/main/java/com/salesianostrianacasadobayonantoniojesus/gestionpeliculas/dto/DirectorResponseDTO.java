package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Director;

import java.util.Set;

public record DirectorResponseDTO(
        Long id,
        String nombre,
        Integer anioNacimiento,
        Set<PeliculaSimpleDTO> peliculas
) {
    public static DirectorResponseDTO of (Director director){
        return new DirectorResponseDTO(
                director.getId(),
                director.getNombre(),
                director.getAnioNacimiento(),
                director.getPeliculas().stream()
                        .map(PeliculaSimpleDTO::of)
                        .collect(java.util.stream.Collectors.toSet())
        );
    }
}
