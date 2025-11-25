package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.controller;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.repository.PeliculaRepository;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.service.PeliculaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PeliculaController {

    private final PeliculaService peliculaService;
}
