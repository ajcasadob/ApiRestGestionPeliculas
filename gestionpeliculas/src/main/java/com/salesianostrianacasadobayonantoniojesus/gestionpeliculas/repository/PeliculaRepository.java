package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.repository;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PeliculaRepository extends JpaRepository<Pelicula,Long> {

    boolean existsByTitulo(String titulo);

}
