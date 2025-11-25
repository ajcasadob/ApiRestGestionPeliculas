package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error;

public class EntidadNoEncontradaException extends RuntimeException {
    public EntidadNoEncontradaException(String message) {
        super(message);
    }
    public EntidadNoEncontradaException(Long id) {
        super("Entidad no encontrada"+id);
    }
    public EntidadNoEncontradaException() {
        super("Entidad no encontrada");
    }
}
