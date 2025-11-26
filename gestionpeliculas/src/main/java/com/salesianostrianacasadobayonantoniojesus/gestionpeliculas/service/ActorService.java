package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.service;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto.ActorRequestDTO;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error.ActorYaEnRepartoException;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error.EntidadNoEncontradaException;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Actor;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Pelicula;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.repository.ActorRepository;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.repository.PeliculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActorService {


    private final ActorRepository actorRepository;
    private final PeliculaRepository peliculaRepository;


   public List<Actor> getAll(){
       List<Actor> result = actorRepository.findAll();

       if(result.isEmpty()){
           throw new EntidadNoEncontradaException("No se han encontrado actores");
       }
       return result;
   }
   public Actor getById(Long id){
       return actorRepository.findById(id)
               .orElseThrow(()-> new EntidadNoEncontradaException("Actor"+id));
   }

   public Actor crear (ActorRequestDTO dto){
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
                .orElseThrow(() -> new EntidadNoEncontradaException("Actor", id));
    }



    public void delete(Long id) {
        if (!actorRepository.existsById(id)) {
            throw new EntidadNoEncontradaException("Actor", id);
        }
        actorRepository.deleteById(id);
    }



    public Pelicula addActor(Long peliculaId, Long actorId) {
        Pelicula p = peliculaRepository.findById(peliculaId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Película", peliculaId));

        Actor a = actorRepository.findById(actorId)
                .orElseThrow(() -> new EntidadNoEncontradaException("Actor", actorId));

        if (p.getActores().contains(a)) {
            throw new ActorYaEnRepartoException(a.getNombre(), p.getTitulo());
        }

        p.getActores().add(a);
        return peliculaRepository.save(p);
    }



}
