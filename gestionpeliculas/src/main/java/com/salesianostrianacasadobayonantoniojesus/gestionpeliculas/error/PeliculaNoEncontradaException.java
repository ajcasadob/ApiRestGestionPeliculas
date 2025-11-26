package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error;

public class PeliculaNoEncontradaException extends EntidadNoEncontradaException{


    public PeliculaNoEncontradaException(String message) {
        super(message);
    }
    public PeliculaNoEncontradaException(Long id) {
        super("Pelicula con ID " + id + " no encontrada");
    }
    public PeliculaNoEncontradaException() {
        super("Pelicula no encontrada");
    }


}
