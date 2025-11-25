package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.service;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.repository.DirectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectorService {


    private final DirectorRepository directorRepository;
}
