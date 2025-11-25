package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto;


import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Director;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Pelicula;

import java.time.LocalDate;

public record PeliculaRequestDTO(
        String titulo,
        String genero,
        LocalDate fechaEstreno,
        Long directorId


) {

    public Pelicula toEntity(Director director) {
        return Pelicula.builder()
                .titulo(titulo)
                .genero(genero)
                .fechaEstreno(fechaEstreno)
                .director(director)
                .build();
    }
}
