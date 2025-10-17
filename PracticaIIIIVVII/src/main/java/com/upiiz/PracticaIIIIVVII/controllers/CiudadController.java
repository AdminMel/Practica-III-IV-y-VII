package com.upiiz.PracticaIIIIVVII.controllers;

import com.upiiz.PracticaIIIIVVII.models.Ciudad;
import com.upiiz.PracticaIIIIVVII.services.CiudadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/rutas/usuarios/v1/ciudades")
public class CiudadController {

    private final CiudadService service;

    public CiudadController(CiudadService service) { this.service = service; }

    // DTO básico de salida (record interno solo para response)
    public record CiudadBasicDto(Long id, String codigo, String nombre) {
        public static CiudadBasicDto from(Ciudad c) {
            return new CiudadBasicDto(c.getId(), c.getCodigo(), c.getNombre());
        }
    }

    @Operation(
            summary = "Obtener listado de Ciudades",
            description = "Devuelve todas las ciudades registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ciudades encontradas :D",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            [
                                              {
                                                "id": "int",
                                                "codigo": "String",
                                                "nombre": "String"
                                              }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor D:",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            { "estado": 0, "mensaje": "Error interno del servidor D:" }
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<?> list() {
        try {
            List<CiudadBasicDto> out = new ArrayList<>();
            service.getAllCiudades().forEach(c -> out.add(CiudadBasicDto.from(c)));
            return ResponseEntity.ok(out);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }

    // =========================
    // GET BY ID
    // =========================
    @Operation(
            summary = "Obtener una Ciudad por ID",
            description = "Devuelve la información de una ciudad específica según su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ciudad encontrada :D",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            {
                                              "ciudad": {
                                                "id": "int",
                                                "codigo": "String",
                                                "nombre": "String"
                                              },
                                              "estado": 1,
                                              "mensaje": "Ciudad encontrada :D"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Ciudad no encontrada D:",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            { "estado": 0, "mensaje": "Ciudad no encontrada D:" }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Falta el ID o formato inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            { "estado": 0, "mensaje": "Falta el ID o el formato es inválido" }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor D:",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            { "estado": 0, "mensaje": "Error interno del servidor D:" }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("estado", 0, "mensaje", "Falta el ID o el formato es inválido"));
            }
            Ciudad ciudad = service.getCiudadById(id);
            if (ciudad == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("estado", 0, "mensaje", "Ciudad no encontrada D:"));
            }
            return ResponseEntity.ok(Map.of(
                    "estado", 1,
                    "mensaje", "Ciudad encontrada :D",
                    "ciudad", CiudadBasicDto.from(ciudad)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }

    // =========================
    // CREATE
    // =========================
    @Operation(
            summary = "Crear una nueva Ciudad",
            description = "Crea una nueva ciudad con su código y nombre.",
            requestBody = @RequestBody(
                    description = "Datos necesarios para registrar la ciudad.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema( // <- forzamos el ejemplo del body aunque el modelo sea Ciudad
                                    example = """
                                            {
                                              "codigo": "String",
                                              "nombre": "String"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Ciudad creada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            {
                                              "id": "int",
                                              "codigo": "String",
                                              "nombre": "String"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor D:",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            { "estado": 0, "mensaje": "Error interno del servidor D:" }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Ciudad body) {
        try {
            Ciudad saved = service.addCiudad(body);
            URI loc = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(saved.getId()).toUri();
            return ResponseEntity.created(loc).body(CiudadBasicDto.from(saved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }

    // =========================
    // UPDATE
    // =========================
    @Operation(
            summary = "Actualizar una Ciudad existente",
            description = "Modifica los datos de una ciudad existente.",
            requestBody = @RequestBody(
                    description = "Datos para actualizar la ciudad.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            {
                                              "codigo": "String",
                                              "nombre": "String"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ciudad actualizada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            {
                                              "id": "int",
                                              "codigo": "String",
                                              "nombre": "String"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Ciudad no encontrada D:",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            { "estado": 0, "mensaje": "Ciudad no encontrada D:" }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor D:",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            { "estado": 0, "mensaje": "Error interno del servidor D:" }
                                            """
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Ciudad body) {
        try {
            Ciudad updated = service.updateCiudad(id, body);
            return ResponseEntity.ok(CiudadBasicDto.from(updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }

    @Operation(
            summary = "Eliminar una Ciudad",
            description = "Elimina una ciudad existente por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ciudad eliminada correctamente (sin contenido)"),
            @ApiResponse(responseCode = "404",
                    description = "Ciudad no encontrada D:",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    { "estado": 0, "mensaje": "Ciudad no encontrada D:" }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor D:",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    { "estado": 0, "mensaje": "Error interno del servidor D:" }
                                    """)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deleteCiudad(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }
}
