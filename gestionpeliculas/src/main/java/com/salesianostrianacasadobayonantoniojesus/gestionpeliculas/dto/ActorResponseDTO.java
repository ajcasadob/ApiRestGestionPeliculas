package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Actor;

public record ActorResponseDTO(
        Long id,
        String nombre
) {
    public static ActorResponseDTO of (Actor actor){
        return new ActorResponseDTO(
                actor.getId(),
                actor.getNombre()
        );
    }
}
