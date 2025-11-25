package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.controller;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.repository.DirectorRepository;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.service.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;
}
