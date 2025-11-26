package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Actor;

import java.util.Set;

public record ActorResponseDTO(
        Long id,
        String nombre,
        Set<PeliculaSimpleDTO> peliculas
) {
    public static ActorResponseDTO of (Actor actor){
        return new ActorResponseDTO(
                actor.getId(),
                actor.getNombre(),
                actor.getPeliculas().stream().map(
                        PeliculaSimpleDTO::of ).collect(java.util.stream.Collectors.toSet()
                )


        );
    }
}
