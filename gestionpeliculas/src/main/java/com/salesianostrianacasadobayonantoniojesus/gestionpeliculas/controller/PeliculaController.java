package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.controller;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto.PeliculaRequestDTO;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto.PeliculaResponseDTO;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Pelicula;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.repository.PeliculaRepository;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.service.PeliculaService;
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
@Tag(name = "Películas", description = "El controlador de películas, para poder realizar las operaciones de gestión")
@RequestMapping("/api/v1/peliculas")
public class PeliculaController {

    private final PeliculaService peliculaService;


    @GetMapping
    @Operation(summary = "Endpoint para obtener todas las películas almacenadas")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Se han encontrado películas",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PeliculaResponseDTO.class)),
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "id": 1,
                                            "titulo": "Inception",
                                            "genero": "Ciencia Ficción",
                                            "fechaEstreno": "2010-07-16",
                                            "director": {
                                                "id": 1,
                                                "nombre": "Christopher Nolan",
                                                "anioNacimiento": 1970
                                            },
                                            "actores": [
                                                {"id": 1, "nombre": "Leonardo DiCaprio"},
                                                {"id": 2, "nombre": "Ellen Page"}
                                            ]
                                        },
                                        {
                                            "id": 2,
                                            "titulo": "Interstellar",
                                            "genero": "Ciencia Ficción",
                                            "fechaEstreno": "2014-11-07",
                                            "director": {
                                                "id": 1,
                                                "nombre": "Christopher Nolan",
                                                "anioNacimiento": 1970
                                            },
                                            "actores": [
                                                {"id": 3, "nombre": "Matthew McConaughey"}
                                            ]
                                        }
                                    ]
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se han encontrado películas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://api.peliculas.com/errors/not-found",
                                                "title": "Entidad no encontrada",
                                                "status": 404,
                                                "detail": "No se encontraron películas",
                                                "instance": "/api/v1/peliculas"
                                            }
                                            """
                            )
                    )
            )
    })
    public List<PeliculaResponseDTO> getAll() {
        return peliculaService.getAll()
                .stream()
                .map(PeliculaResponseDTO::of)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Endpoint para obtener una película por su ID, incluyendo su director y reparto completo")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Película encontrada con su director y reparto",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PeliculaResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id": 1,
                                                "titulo": "Inception",
                                                "genero": "Ciencia Ficción",
                                                "fechaEstreno": "2010-07-16",
                                                "director": {
                                                    "id": 1,
                                                    "nombre": "Christopher Nolan",
                                                    "anioNacimiento": 1970
                                                },
                                                "actores": [
                                                    {"id": 1, "nombre": "Leonardo DiCaprio"},
                                                    {"id": 2, "nombre": "Ellen Page"},
                                                    {"id": 3, "nombre": "Tom Hardy"}
                                                ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Película no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://api.peliculas.com/errors/not-found",
                                                "title": "Entidad no encontrada",
                                                "status": 404,
                                                "detail": "Película con ID 99 no encontrado",
                                                "instance": "/api/v1/peliculas/99"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<PeliculaResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                PeliculaResponseDTO.of(peliculaService.getById(id))
        );
    }

    @PostMapping
    @Operation(summary = "Endpoint para crear una nueva película. Requiere el ID de un director existente")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Película creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PeliculaResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id": 1,
                                                "titulo": "Inception",
                                                "genero": "Ciencia Ficción",
                                                "fechaEstreno": "2010-07-16",
                                                "director": {
                                                    "id": 1,
                                                    "nombre": "Christopher Nolan",
                                                    "anioNacimiento": 1970
                                                },
                                                "actores": []
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Director no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://api.peliculas.com/errors/not-found",
                                                "title": "Entidad no encontrada",
                                                "status": 404,
                                                "detail": "Director con ID 99 no encontrado",
                                                "instance": "/api/v1/peliculas"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Ya existe una película con ese título",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://api.peliculas.com/errors/pelicula-duplicada",
                                                "title": "Película ya existe",
                                                "status": 409,
                                                "detail": "Esta pelicula ya existe",
                                                "instance": "/api/v1/peliculas"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "El director tenía menos de 18 años al estrenar la película, o faltan datos obligatorios",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://api.peliculas.com/errors/director-menor-edad",
                                                "title": "Director menor de edad",
                                                "status": 400,
                                                "detail": "El director 'Christopher Nolan' con edad 17 no puede dirigir una pelicula estrenada en 1987 porque es menor de edad",
                                                "instance": "/api/v1/peliculas"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<PeliculaResponseDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear una nueva película. Debe incluir el ID de un director existente",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PeliculaRequestDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "titulo": "Inception",
                                                "genero": "Ciencia Ficción",
                                                "fechaEstreno": "2010-07-16",
                                                "directorId": 1
                                            }
                                            """
                            )
                    )
            )
            @RequestBody PeliculaRequestDTO dto
    ) {
        Pelicula nueva = peliculaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PeliculaResponseDTO.of(nueva));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Endpoint para editar una película existente")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Película actualizada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PeliculaResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id": 1,
                                                "titulo": "Inception - Directors Cut",
                                                "genero": "Ciencia Ficción",
                                                "fechaEstreno": "2010-07-16",
                                                "director": {
                                                    "id": 1,
                                                    "nombre": "Christopher Nolan",
                                                    "anioNacimiento": 1970
                                                },
                                                "actores": [
                                                    {"id": 1, "nombre": "Leonardo DiCaprio"}
                                                ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Película o director no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://api.peliculas.com/errors/not-found",
                                                "title": "Entidad no encontrada",
                                                "status": 404,
                                                "detail": "Película con ID 99 no encontrado",
                                                "instance": "/api/v1/peliculas/99"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "El nuevo título ya existe en otra película",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://api.peliculas.com/errors/pelicula-duplicada",
                                                "title": "Película ya existe",
                                                "status": 409,
                                                "detail": "Esta pelicula ya existe",
                                                "instance": "/api/v1/peliculas/1"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "El director tenía menos de 18 años al estrenar la película",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://api.peliculas.com/errors/director-menor-edad",
                                                "title": "Director menor de edad",
                                                "status": 400,
                                                "detail": "El director 'Christopher Nolan' con edad 17 no puede dirigir una pelicula estrenada en 1987 porque es menor de edad",
                                                "instance": "/api/v1/peliculas/1"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<PeliculaResponseDTO> edit(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para actualizar la película",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PeliculaRequestDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "titulo": "Inception - Directors Cut",
                                                "genero": "Ciencia Ficción",
                                                "fechaEstreno": "2010-07-16",
                                                "directorId": 1
                                            }
                                            """
                            )
                    )
            )
            @RequestBody PeliculaRequestDTO dto
    ) {
        return ResponseEntity.ok(
                PeliculaResponseDTO.of(
                        peliculaService.edit(id, dto)
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Endpoint para eliminar una película")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Película eliminada exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Película no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://api.peliculas.com/errors/not-found",
                                                "title": "Entidad no encontrada",
                                                "status": 404,
                                                "detail": "Película con ID 99 no encontrado",
                                                "instance": "/api/v1/peliculas/99"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<?> delete(@PathVariable Long id) {
        peliculaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{peliculaId}/actores/{actorId}")
    @Operation(
            summary = "Endpoint para asignar un actor al reparto de una película",
            description = "Asigna un actor existente a una película existente. Si el actor ya está en el reparto, devuelve error 409 Conflict"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Actor asignado exitosamente al reparto de la película",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PeliculaResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id": 1,
                                                "titulo": "Inception",
                                                "genero": "Ciencia Ficción",
                                                "fechaEstreno": "2010-07-16",
                                                "director": {
                                                    "id": 1,
                                                    "nombre": "Christopher Nolan",
                                                    "anioNacimiento": 1970
                                                },
                                                "actores": [
                                                    {"id": 1, "nombre": "Leonardo DiCaprio"},
                                                    {"id": 2, "nombre": "Ellen Page"}
                                                ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Película o actor no encontrado",
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
                                                "instance": "/api/v1/peliculas/1/actores/99"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "El actor ya está asignado al reparto de esta película",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://api.peliculas.com/errors/actor-duplicado",
                                                "title": "Actor ya existe en el reparto",
                                                "status": 409,
                                                "detail": "El actor 'Leonardo DiCaprio' ya está asignado a la película 'Inception'",
                                                "instance": "/api/v1/peliculas/1/actores/1"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<PeliculaResponseDTO> addActorToPelicula(
            @PathVariable Long peliculaId,
            @PathVariable Long actorId
    ) {
        Pelicula actualizada = peliculaService.addActorToPelicula(peliculaId, actorId);
        return ResponseEntity.ok(PeliculaResponseDTO.of(actualizada));
    }

}
