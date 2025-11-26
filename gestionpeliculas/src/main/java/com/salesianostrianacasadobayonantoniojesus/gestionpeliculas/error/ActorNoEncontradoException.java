package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error;

public class ActorNoEncontradoException extends EntidadNoEncontradaException {


    public ActorNoEncontradoException(String message) {
        super(message);
    }
    public ActorNoEncontradoException() {
        super("Actor no encontrado");
    }
    public ActorNoEncontradoException(Long id) {
        super("Actor con ID " + id + " no encontrado");
    }
}
