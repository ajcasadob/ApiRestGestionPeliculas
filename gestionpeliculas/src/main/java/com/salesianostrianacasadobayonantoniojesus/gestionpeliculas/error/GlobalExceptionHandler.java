package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    private static final String BASE_URI = "https://api.peliculas.com/errors";

    @ExceptionHandler(EntidadNoEncontradaException.class)
    public ProblemDetail  handleEntidadNotFound (EntidadNoEncontradaException ex){


        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage()


        );

        problemDetail.setTitle("Entidad no encontrada");
        problemDetail.setType(URI.create(BASE_URI+"/not-found"));

        return problemDetail;
    }


    @ExceptionHandler(PeliculaYaExisteException.class)
    public ProblemDetail handlePeliculaYaExiste(PeliculaYaExisteException ex ){

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );

        problemDetail.setTitle("Película ya existe");
        problemDetail.setType(URI.create(BASE_URI+"/pelicula-duplicada"));

        return problemDetail;

    }


    @ExceptionHandler(ActorYaEnRepartoException.class)
    public ProblemDetail handleActorYaEnReparto(ActorYaEnRepartoException ex){

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );

        problemDetail.setTitle("Actor ya existe en el reparto");
        problemDetail.setType(URI.create(BASE_URI+"/actor-duplicado"));

        return problemDetail;
    }


    @ExceptionHandler(DirectorMenorEdadExcepetion.class)
    public ProblemDetail handleDirectorMenorEdad(DirectorMenorEdadExcepetion ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );

        problemDetail.setTitle("Director menor de edad");
        problemDetail.setType(URI.create(BASE_URI + "/director-menor-edad"));

        return problemDetail;
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumnent(IllegalArgumentException ex){

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );

        problemDetail.setTitle("Argumento inválido");
        problemDetail.setType(URI.create(BASE_URI+"/bad-request"));

        return problemDetail;
    }

}
