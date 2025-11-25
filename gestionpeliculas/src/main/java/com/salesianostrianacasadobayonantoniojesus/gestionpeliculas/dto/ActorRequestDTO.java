package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Actor;

public record ActorRequestDTO(
        String nombre
) {

    public Actor toEntity(){

            return Actor.builder()
                    .nombre(nombre)
                    .build();
        }
    }

