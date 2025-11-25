package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error;

public class DirectorMenorEdadExcepetion extends RuntimeException {


    public DirectorMenorEdadExcepetion(String message) {
        super(message);
    }

    public DirectorMenorEdadExcepetion(Long id) {
        super("Este director es menor de edad"+id);
    }

    public DirectorMenorEdadExcepetion() {
        super("Este director es menor de edad");
    }

}
