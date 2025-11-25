package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error;

public class ActorYaEnRepartoException extends RuntimeException {

    public ActorYaEnRepartoException(String message) {
        super(message);
    }

    public ActorYaEnRepartoException(Long id) {
        super("Este actor ya esta en reparto "+id);
    }

    public ActorYaEnRepartoException() {
        super("Este actor ya esta en reparto");
    }

}
