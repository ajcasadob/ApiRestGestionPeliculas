package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error;

public class PeliculaYaExisteException extends RuntimeException {


    public PeliculaYaExisteException(String message) {
        super(message);
    }
    public PeliculaYaExisteException(Long id) {
        super("Esta pelicula ya existe"+id);
    }
    public PeliculaYaExisteException() {
        super("Esta pelicula ya existe");
    }



}
