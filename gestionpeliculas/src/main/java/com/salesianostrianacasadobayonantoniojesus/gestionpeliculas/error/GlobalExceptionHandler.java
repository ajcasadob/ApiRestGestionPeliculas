package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntidadNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail  handleEntidadNotFound (EntidadNoEncontradaException ex){


        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage()


        );

        problem.setTitle("Entidad no encontrada");
        problem.setType(URI.create("http://dam.salesianos-triana.com/entidad-no-encontrada"));

        return problem;
    }

    @ExceptionHandler(ActorYaEnRepartoException.class)
    public ProblemDetail handleActorYaEnReparto (ActorYaEnRepartoException ex){
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage()
        );

        problem.setTitle("Actor ya en reparto");
        problem.setType(URI.create("http://dam.salesianos-triana.com/actor-en-reparto"));

        return problem;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleDirectorMenorEdadException (IllegalArgumentException ex){

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                ex.getMessage());

        return problem;
    }

}
