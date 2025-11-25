package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Pelicula;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public record PeliculaResponseDTO(
        Long id,
        String titulo,
        String genero,
        LocalDate fechaEstreno,
        DirectorSimpleDTO director,
        Set<ActorSimpleDTO> actores
) {

    public static PeliculaResponseDTO of (Pelicula pelicula){
        return new PeliculaResponseDTO(
                pelicula.getId(),
                pelicula.getTitulo(),
                pelicula.getGenero(),
                pelicula.getFechaEstreno(),
                DirectorSimpleDTO.of(pelicula.getDirector()),
                pelicula.getActores()
                        .stream()
                        .map(ActorSimpleDTO::of)
                        .collect(Collectors.toSet())
        );
    }
}
