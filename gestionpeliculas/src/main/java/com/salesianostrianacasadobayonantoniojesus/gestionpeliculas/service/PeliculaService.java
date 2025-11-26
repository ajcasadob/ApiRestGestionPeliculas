package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.service;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto.PeliculaRequestDTO;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error.DirectorMenorEdadExcepetion;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error.EntidadNoEncontradaException;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error.PeliculaYaExisteException;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Actor;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Director;
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


    private final PeliculaRepository peliculaRepository;
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
                .orElseThrow(() -> new EntidadNoEncontradaException("Película", id));
    }

    public Pelicula create(PeliculaRequestDTO dto) {
        if (!StringUtils.hasText(dto.titulo())) {
            throw new IllegalArgumentException("Falta el campo del título de la película");
        }

        if (peliculaRepository.existsByTitulo(dto.titulo())) {
            throw new PeliculaYaExisteException(dto.titulo());
        }

        Director d = directorRepository.findById(dto.directorId())
                .orElseThrow(() -> new EntidadNoEncontradaException("Director", dto.directorId()));

        validarEdadDirector(d, dto.fechaEstreno().getYear());

        return peliculaRepository.save(dto.toEntity(d));
    }


    public Pelicula edit(Long idPelicula, PeliculaRequestDTO dto) {
        if (!StringUtils.hasText(dto.titulo())) {
            throw new IllegalArgumentException("Falta el campo del título de la película");
        }

        Pelicula peliculaActual = peliculaRepository.findById(idPelicula)
                .orElseThrow(() -> new EntidadNoEncontradaException("Película", idPelicula));


        if (!peliculaActual.getTitulo().equals(dto.titulo()) &&
                peliculaRepository.existsByTitulo(dto.titulo())) {
            throw new PeliculaYaExisteException(dto.titulo());
        }

        Director d = directorRepository.findById(dto.directorId())
                .orElseThrow(() -> new EntidadNoEncontradaException("Director", dto.directorId()));

        validarEdadDirector(d, dto.fechaEstreno().getYear());

        peliculaActual.setTitulo(dto.titulo());
        peliculaActual.setGenero(dto.genero());
        peliculaActual.setFechaEstreno(dto.fechaEstreno());
        peliculaActual.setDirector(d);

        return peliculaRepository.save(peliculaActual);
    }


    public void delete(Long id) {
        Pelicula p = peliculaRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Película", id));
        peliculaRepository.deleteById(id);
    }

    private void validarEdadDirector(Director director, int anioEstreno) {
        int edadDirector = anioEstreno - director.getAnioNacimiento();

        if (edadDirector < 18) {
            throw new DirectorMenorEdadExcepetion(
                    director.getNombre(),
                    edadDirector,
                    anioEstreno
            );
        }
    }




}
