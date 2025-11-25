package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Pelicula;

import java.time.LocalDate;

public record PeliculaSimpleDTO(
        Long id,
        String titulo,
        String genero,
        LocalDate fechaEstreno

) {
    public static PeliculaSimpleDTO of (Pelicula pelicula){
        return new PeliculaSimpleDTO(
                pelicula.getId(),
                pelicula.getTitulo(),
                pelicula.getGenero(),
                pelicula.getFechaEstreno()
        );
    }
}
