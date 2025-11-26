package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error;

public class DirectorNoEncontradoException extends EntidadNoEncontradaException {


    public DirectorNoEncontradoException(String message) {
        super(message);
    }
    public DirectorNoEncontradoException(Long id) {
        super("Director con ID " + id + " no encontrado");
    }
    public DirectorNoEncontradoException() {
        super("Director no encontrado");
    }
}
