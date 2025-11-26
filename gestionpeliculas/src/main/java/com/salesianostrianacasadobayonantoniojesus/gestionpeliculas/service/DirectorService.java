package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.service;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto.DirectorRequestDTO;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error.DirectorNoEncontradoException;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error.EntidadNoEncontradaException;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Director;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.repository.DirectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;

    public List<Director> getAll(){
        List<Director> result = directorRepository.findAll();

        if(result.isEmpty()){
            throw new DirectorNoEncontradoException("No se han encontrado directores");
        }
        return result;
    }

    public Director getById(Long id) {
        return directorRepository.findById(id)
                .orElseThrow(() -> new DirectorNoEncontradoException(id));
    }

    public Director create(DirectorRequestDTO dto) {
        if (!StringUtils.hasText(dto.nombre())) {
            throw new IllegalArgumentException("Falta el campo del nombre del director");
        }

        return directorRepository.save(dto.toEntity());
    }

    public Director edit(Long id, DirectorRequestDTO dto) {
        if (!StringUtils.hasText(dto.nombre())) {
            throw new IllegalArgumentException("Falta el campo del nombre del director");
        }

        return directorRepository.findById(id)
                .map(d -> {
                    d.setNombre(dto.nombre());
                    d.setAnioNacimiento(dto.anioNacimiento());
                    return directorRepository.save(d);
                })
                .orElseThrow(() -> new DirectorNoEncontradoException(id));
    }

    public void delete(Long id) {
        Director d = directorRepository.findById(id)
                .orElseThrow(() -> new DirectorNoEncontradoException(id));
        directorRepository.deleteById(id);
    }


}
