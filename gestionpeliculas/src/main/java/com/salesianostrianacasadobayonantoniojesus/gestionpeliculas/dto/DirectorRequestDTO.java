package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Actor;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Director;

public record DirectorRequestDTO(
        String nombre,
        Integer anioNacimiento
) {

    public Director toEntity(){

            return Director.builder()
                    .nombre(nombre)
                    .anioNacimiento(anioNacimiento)
                    .build();
        }
    }



