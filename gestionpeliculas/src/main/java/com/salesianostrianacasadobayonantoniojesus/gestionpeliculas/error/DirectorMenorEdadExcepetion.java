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


    public DirectorMenorEdadExcepetion(String nombre, int edad, int anioEstreno) {
        super(String.format("El director '%s' con edad %d no puede dirigir una pelicula estrenada en %d porque es menor de edad", nombre, edad, anioEstreno));
    }
}
