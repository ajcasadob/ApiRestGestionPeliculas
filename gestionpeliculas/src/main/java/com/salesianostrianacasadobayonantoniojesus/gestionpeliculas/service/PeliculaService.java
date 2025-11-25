package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.service;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.repository.PeliculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PeliculaService {


    private  final PeliculaRepository peliculaRepository;
}
