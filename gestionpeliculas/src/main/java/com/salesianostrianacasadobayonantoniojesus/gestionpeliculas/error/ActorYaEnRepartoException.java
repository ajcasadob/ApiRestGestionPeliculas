package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error;

public class ActorYaEnRepartoException extends RuntimeException {

    public ActorYaEnRepartoException(String mensaje) {
        super(mensaje);
    }

    public ActorYaEnRepartoException(String nombreActor, String tituloPelicula) {
        super(String.format("El actor '%s' ya está asignado a la película '%s'",
                nombreActor, tituloPelicula));
    }

}
