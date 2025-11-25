package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.repository;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Director;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<Director,Long> {
}
