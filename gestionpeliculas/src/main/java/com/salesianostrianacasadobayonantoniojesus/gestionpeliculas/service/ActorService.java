package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.service;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto.ActorRequestDTO;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error.ActorNoEncontradoException;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error.EntidadNoEncontradaException;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Actor;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.repository.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActorService {


    private final ActorRepository actorRepository;

    public List<Actor> getAll(){
        List<Actor> result = actorRepository.findAll();

        if(result.isEmpty()){
            throw new ActorNoEncontradoException("No se han encontrado actores");
        }
        return result;
    }

    public Actor getById(Long id){
        return actorRepository.findById(id)
                .orElseThrow(()-> new ActorNoEncontradoException(id));
    }

    public Actor crear(ActorRequestDTO dto){
        if(!StringUtils.hasText(dto.nombre())){
            throw new IllegalArgumentException("Falta el campo del nombre del actor");
        }
        return actorRepository.save(dto.toEntity());
    }

    public Actor edit(Long id, ActorRequestDTO dto) {
        if (!StringUtils.hasText(dto.nombre())) {
            throw new IllegalArgumentException("Falta el campo del nombre del actor");
        }

        return actorRepository.findById(id)
                .map(a -> {
                    a.setNombre(dto.nombre());
                    return actorRepository.save(a);
                })
                .orElseThrow(() -> new ActorNoEncontradoException(id));
    }

    public void delete(Long id) {
        if (!actorRepository.existsById(id)) {
            throw new ActorNoEncontradoException(id);
        }
        actorRepository.deleteById(id);
    }



}
