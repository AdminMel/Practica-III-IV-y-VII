package com.upiiz.PracticaIIIIVVII.controllers;

import com.upiiz.PracticaIIIIVVII.models.Camion;
import com.upiiz.PracticaIIIIVVII.services.CamionService;
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
@RequestMapping("/rutas/usuarios/v1/camiones")
public class CamionController {

    private final CamionService service;

    public CamionController(CamionService service) { this.service = service; }

    public record CamionBasicDto(Long id, String matricula, String modelo, String potencia, String tipo) {
        public static CamionBasicDto from(Camion c) {
            return new CamionBasicDto(c.getId(), c.getMatricula(), c.getModelo(), c.getPotencia(), c.getTipo());
        }
    }

    @Operation(
            summary = "Obtener listado de Camiones",
            description = "Devuelve todos los camiones registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Camiones encontrados :D",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            [
                                              {
                                                "id": "int",
                                                "matricula": "String",
                                                "modelo": "String",
                                                "potencia": "String",
                                                "tipo": "String"
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
                            schema = @Schema(example = """
                                    { "estado": 0, "mensaje": "Error interno del servidor D:" }
                                    """)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<?> list() {
        try {
            List<CamionBasicDto> out = new ArrayList<>();
            service.getAllCamioness().forEach(c -> out.add(CamionBasicDto.from(c)));
            return ResponseEntity.ok(out);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }

    @Operation(
            summary = "Obtener un Camión por ID",
            description = "Devuelve la información de un camión según su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Camión encontrado :D",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "camion": {
                                        "id": "int",
                                        "matricula": "String",
                                        "modelo": "String",
                                        "potencia": "String",
                                        "tipo": "String"
                                      },
                                      "estado": 1,
                                      "mensaje": "Camión encontrado :D"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Camión no encontrado D:",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    { "estado": 0, "mensaje": "Camión no encontrado D:" }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Falta el ID o formato inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    { "estado": 0, "mensaje": "Falta el ID o el formato es inválido" }
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
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("estado", 0, "mensaje", "Falta el ID o el formato es inválido"));
            }
            Camion c = service.getCamionById(id);
            if (c == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("estado", 0, "mensaje", "Camión no encontrado D:"));
            }
            return ResponseEntity.ok(Map.of(
                    "estado", 1,
                    "mensaje", "Camión encontrado :D",
                    "camion", CamionBasicDto.from(c)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }

    @Operation(
            summary = "Crear un nuevo Camión",
            description = "Crea un camión con matrícula, modelo, potencia y tipo.",
            requestBody = @RequestBody(
                    description = "Datos necesarios para registrar el camión.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "matricula": "String",
                                      "modelo": "String",
                                      "potencia": "String",
                                      "tipo": "String"
                                    }
                                    """)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Camión creado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "id": "int",
                                      "matricula": "String",
                                      "modelo": "String",
                                      "potencia": "String",
                                      "tipo": "String"
                                    }
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
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Camion body) {
        try {
            Camion saved = service.addCamion(body);
            URI loc = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(saved.getId()).toUri();
            return ResponseEntity.created(loc).body(CamionBasicDto.from(saved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }

    @Operation(
            summary = "Actualizar un Camión existente",
            description = "Modifica los datos de un camión por su ID.",
            requestBody = @RequestBody(
                    description = "Datos para actualizar el camión.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "matricula": "String",
                                      "modelo": "String",
                                      "potencia": "String",
                                      "tipo": "String"
                                    }
                                    """)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Camión actualizado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "id": "int",
                                      "matricula": "String",
                                      "modelo": "String",
                                      "potencia": "String",
                                      "tipo": "String"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Camión no encontrado D:",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    { "estado": 0, "mensaje": "Camión no encontrado D:" }
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
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Camion body) {
        try {
            Camion updated = service.updateCamion(id, body);
            return ResponseEntity.ok(CamionBasicDto.from(updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }

    @Operation(
            summary = "Eliminar un Camión",
            description = "Elimina un camión existente por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Camión eliminado correctamente (sin contenido)"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Camión no encontrado D:",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    { "estado": 0, "mensaje": "Camión no encontrado D:" }
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
            service.deleteCamion(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }
}
