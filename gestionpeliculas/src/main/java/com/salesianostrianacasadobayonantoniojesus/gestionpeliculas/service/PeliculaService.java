package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.service;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto.PeliculaRequestDTO;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error.EntidadNoEncontradaException;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Pelicula;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.repository.ActorRepository;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.repository.DirectorRepository;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.repository.PeliculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PeliculaService {


    private  final PeliculaRepository peliculaRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;


    public List<Pelicula> getAll() {
        List<Pelicula> result = peliculaRepository.findAll();

        if (result.isEmpty()) {
            throw new EntidadNoEncontradaException("No se encontraron películas");
        }

        return result;
    }

    public Pelicula getById(Long id) {
        return peliculaRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException(id));
    }

    public Pelicula crear (PeliculaRequestDTO dto){

        if(!StringUtils.hasText(dto.titulo())){
            throw new IllegalArgumentException("No existe esta película");
        }
        return peliculaRepository.save(dto.toEntity());

    }

    public Pelicula edit ( Long id, PeliculaRequestDTO dto){

        if(!StringUtils.hasText(dto.titulo())){
            throw new IllegalArgumentException("Falta el título de la pelicula");
        }
        return peliculaRepository.findById(id)
                .map(p ->{

                })
    }



}
