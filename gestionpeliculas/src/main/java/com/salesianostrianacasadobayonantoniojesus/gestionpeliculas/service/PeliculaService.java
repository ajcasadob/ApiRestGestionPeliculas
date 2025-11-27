package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.service;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto.PeliculaRequestDTO;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error.*;
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
            throw new PeliculaNoEncontradaException("No se encontraron películas");
        }

        return result;
    }

    public Pelicula getById(Long id) {
        return peliculaRepository.findById(id)
                .orElseThrow(() -> new PeliculaNoEncontradaException(id));
    }

    public Pelicula create(PeliculaRequestDTO dto) {


        if (!StringUtils.hasText(dto.titulo())) {
            throw new IllegalArgumentException("Falta el campo del título de la película");
        }

        if (peliculaRepository.existsByTitulo(dto.titulo())) {
            throw new PeliculaYaExisteException(dto.titulo());
        }

        Director d = directorRepository.findById(dto.directorId())
                .orElseThrow(() -> new DirectorNoEncontradoException(dto.directorId()));



        if (!d.esMayorDeEdad(dto.fechaEstreno().getYear())) {
            throw new DirectorMenorEdadExcepetion(d.getNombre(), d.calcularEdad(dto.fechaEstreno().getYear()), dto.fechaEstreno().getYear());
        }

        Pelicula p = dto.toEntity();
        p.setDirector(d);

        return peliculaRepository.save(p);
    }

    public Pelicula edit(Long idPelicula, PeliculaRequestDTO dto) {

        if (!StringUtils.hasText(dto.titulo())) {
            throw new IllegalArgumentException("Falta el campo del título de la película");
        }

        Pelicula peliculaActual = peliculaRepository.findById(idPelicula)
                .orElseThrow(() -> new PeliculaNoEncontradaException(idPelicula));

        if (!peliculaActual.getTitulo().equals(dto.titulo()) &&
                peliculaRepository.existsByTitulo(dto.titulo())) {
            throw new PeliculaYaExisteException(dto.titulo());
        }

        Director d = directorRepository.findById(dto.directorId())
                .orElseThrow(() -> new DirectorNoEncontradoException(dto.directorId()));



        if (!d.esMayorDeEdad(dto.fechaEstreno().getYear())) {
            throw new DirectorMenorEdadExcepetion(d.getNombre(), d.calcularEdad(dto.fechaEstreno().getYear()), dto.fechaEstreno().getYear());
        }

        peliculaActual.setTitulo(dto.titulo());
        peliculaActual.setGenero(dto.genero());
        peliculaActual.setFechaEstreno(dto.fechaEstreno());
        peliculaActual.setDirector(d);

        return peliculaRepository.save(peliculaActual);
    }

    public void delete(Long id) {
        Pelicula p = peliculaRepository.findById(id)
                .orElseThrow(() -> new PeliculaNoEncontradaException(id));
        peliculaRepository.deleteById(id);
    }


    public Pelicula addActorToPelicula(Long peliculaId, Long actorId) {
        Pelicula p = peliculaRepository.findById(peliculaId)
                .orElseThrow(() -> new PeliculaNoEncontradaException(peliculaId));

        Actor a = actorRepository.findById(actorId)
                .orElseThrow(() -> new ActorNoEncontradoException(actorId));

        if (p.getActores().contains(a)) {
            throw new ActorYaEnRepartoException(a.getNombre(), p.getTitulo());
        }

        p.addActor(a);

        return peliculaRepository.save(p);
    }


}
