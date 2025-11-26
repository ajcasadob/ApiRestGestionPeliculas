package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.controller;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto.ActorRequestDTO;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto.ActorResponseDTO;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Actor;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.service.ActorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/actores")
@Tag(name = "Actores", description = "El controlador de actores, para poder realizar todas las operaciones de gestión")
public class ActorController {

    private final ActorService actorService;

    @GetMapping
    @Operation(summary = "Endpoint para obtener todos los actores almacenados")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Se han encontrado actores",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ActorResponseDTO.class)),
                            examples = @ExampleObject(value = """
                                    [
                                             {
                                                 "id": 1,
                                                 "nombre": "Leonardo DiCaprio",
                                                 "peliculas": [
                                                     {"id": 1, "titulo": "Inception"},
                                                     {"id": 2, "titulo": "The Wolf of Wall Street"}
                                                 ]
                                             },
                                             {
                                                 "id": 2,
                                                 "nombre": "Ellen Page",
                                                 "peliculas": [
                                                     {"id": 1, "titulo": "Inception"}
                                                 ]
                                             }
                                         ]
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se han encontrado actores",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://api.peliculas.com/errors/not-found",
                                                "title": "Entidad no encontrada",
                                                "status": 404,
                                                "detail": "No se encontraron actores",
                                                "instance": "/api/v1/actores/"
                                            }
                                            """
                            )
                    )
            )
    })
    public List<ActorResponseDTO> getAll() {
        return actorService.getAll()
                .stream()
                .map(ActorResponseDTO::of)
                .toList();
    }

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Actor encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ActorResponseDTO.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "id": 1,
                                                        "nombre": "Leonardo DiCaprio",
                                                        "peliculas": [
                                                        {"id": 1, "titulo": "Inception"},
                                                        {"id": 2, "titulo": "The Wolf of Wall Street"}
                                                        ]
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Actor no encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "type": "https://api.peliculas.com/errors/not-found",
                                                        "title": "Entidad no encontrada",
                                                        "status": 404,
                                                        "detail": "Actor con ID 99 no encontrado",
                                                        "instance": "/api/v1/actores/99"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ActorResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ActorResponseDTO.of(actorService.getById(id))
        );
    }

    @PostMapping
    @Operation(summary = "Endpoint para crear un nuevo actor")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Actor creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ActorResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                               "id": 1,
                                               "nombre": "Leonardo DiCaprio",
                                               "peliculas": []
                                                     }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o falta el nombre del actor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://api.peliculas.com/errors/bad-request",
                                                "title": "Argumento inválido",
                                                "status": 400,
                                                "detail": "Falta el campo del nombre del actor",
                                                "instance": "/api/v1/actores/"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<ActorResponseDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un nuevo actor",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ActorRequestDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "nombre": "Leonardo DiCaprio"
                                            }
                                            """
                            )
                    )
            )
            @RequestBody ActorRequestDTO dto
    ) {
        Actor nuevo = actorService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ActorResponseDTO.of(nuevo));
    }

}
