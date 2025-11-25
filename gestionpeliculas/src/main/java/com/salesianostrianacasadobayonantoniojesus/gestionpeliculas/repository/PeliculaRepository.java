package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.repository;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeliculaRepository extends JpaRepository<Pelicula,Long> {
}
