package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error;

public class EntidadNoEncontradaException extends RuntimeException {
    public EntidadNoEncontradaException(String mensaje) {
        super(mensaje);
    }

    public EntidadNoEncontradaException(String entidad, Long id) {
        super(String.format("%s con ID %d no encontrado", entidad, id));
    }


}
