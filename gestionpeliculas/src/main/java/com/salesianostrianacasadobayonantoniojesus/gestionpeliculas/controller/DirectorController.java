package com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.controller;

import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto.DirectorRequestDTO;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.dto.DirectorResponseDTO;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.model.Director;
import com.salesianostrianacasadobayonantoniojesus.gestionpeliculas.service.DirectorService;
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
@RequestMapping("/api/v1/directores")
@Tag(name = "Directores", description = "El controlador de directores  para poder realizar todas las operaciones de gestión")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping
    @Operation(summary = "Endpoint para obtener todos los directores almacenados")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Se han encontrado directores",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DirectorResponseDTO.class)),
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "id": 1,
                                            "nombre": "Christopher Nolan",
                                            "anioNacimiento": 1970,
                                            "peliculas": [
                                            {"id": 1, "titulo": "Inception"},
                                            {"id": 2, "titulo": "Interstellar"}
                                             ]
                                        },
                                        {
                                            "id": 2,
                                            "nombre": "Steven Spielberg",
                                            "anioNacimiento": 1946,
                                            "peliculas":[
                                            {"id": 3, "titulo": "Jurassic Park"}
                                    
                                            ]
                                        }
                                    ]
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se han encontrado directores",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://api.peliculas.com/errors/not-found",
                                                "title": "Entidad no encontrada",
                                                "status": 404,
                                                "detail": "No se encontraron directores",
                                                "instance": "/api/v1/directores/"
                                            }
                                            """
                            )
                    )
            )
    })
    public List<DirectorResponseDTO> getAll() {
        return directorService.getAll()
                .stream()
                .map(DirectorResponseDTO::of)
                .toList();
    }

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Director encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DirectorResponseDTO.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                        "id": 1,
                                                        "nombre": "Christopher Nolan",
                                                        "anioNacimiento": 1970,
                                                        "peliculas": [
                                                             {"id": 1, "titulo": "Inception"},
                                                             {"id": 2, "titulo": "Interstellar"}
                                                             ]
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
                                                        "detail": "Director con ID 1 no encontrado",
                                                        "instance": "/api/v1/directores/1"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @Operation(summary = "Endpoint para obtener un director por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<DirectorResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                DirectorResponseDTO.of(directorService.getById(id))
        );
    }

    @PostMapping
    @Operation(summary = "Endpoint para crear un nuevo director")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Director creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DirectorResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                        "id": 1,
                        "nombre": "Christopher Nolan",
                        "anioNacimiento": 1970,
                        "peliculas": []
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o falta el nombre del director",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                        "type": "https://api.peliculas.com/errors/bad-request",
                        "title": "Argumento inválido",
                        "status": 400,
                        "detail": "Falta el campo del nombre del director",
                        "instance": "/api/v1/directores"
                    }
                    """
                            )
                    )
            )
    })

    public ResponseEntity<DirectorResponseDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un nuevo director",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = DirectorRequestDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "nombre": "Christopher Nolan",
                                                "anioNacimiento": 1970
                                            }
                                            """
                            )
                    )
            )
            @RequestBody DirectorRequestDTO dto
    ) {
        Director nuevo = directorService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DirectorResponseDTO.of(nuevo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Endpoint para editar un director existente")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Director actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DirectorResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                        "id": 1,
                        "nombre": "Christopher Nolan",
                        "anioNacimiento": 1970,
                        "peliculas": [
                            {"id": 1, "titulo": "Inception"}
                        ]
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
                        "instance": "/api/v1/directores/99"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })

    public ResponseEntity<DirectorResponseDTO> edit(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para actualizar el director",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = DirectorRequestDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "nombre": "Christopher Nolan",
                                                "anioNacimiento": 1970
                                            }
                                            """
                            )
                    )
            )
            @RequestBody DirectorRequestDTO dto
    ) {
        return ResponseEntity.ok(
                DirectorResponseDTO.of(
                        directorService.edit(id, dto)
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Endpoint para eliminar un director")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Director eliminado exitosamente"
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
                        "instance": "/api/v1/directores/99"
                    }
                    """
                            )
                    )
            )
    })
        public ResponseEntity<?> delete(@PathVariable Long id) {
        directorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
