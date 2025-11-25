package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Director;

public record DirectorResponseDTO(
        Long id,
        String nombre,
        Integer anioNacimiento
) {
    public static DirectorResponseDTO of (Director director){
        return new DirectorResponseDTO(
                director.getId(),
                director.getNombre(),
                director.getAnioNacimiento()
        );
    }
}
