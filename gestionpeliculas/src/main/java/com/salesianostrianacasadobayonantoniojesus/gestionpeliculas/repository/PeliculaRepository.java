package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.repository;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PeliculaRepository extends JpaRepository<Pelicula,Long> {

    boolean existsByTitulo(String titulo);
    boolean existsByTituloIgnoreCase(String titulo);            // si quieres case-insensitive
    boolean existsByTituloAndIdNot(String titulo, Long id);     // útil en actualizaciones
    Optional<Pelicula> findByTitulo(String titulo);
}
