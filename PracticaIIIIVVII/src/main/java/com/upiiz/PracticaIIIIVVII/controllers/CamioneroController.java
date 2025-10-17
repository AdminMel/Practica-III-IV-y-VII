package com.upiiz.PracticaIIIIVVII.controllers;

import com.upiiz.PracticaIIIIVVII.models.Camionero;
import com.upiiz.PracticaIIIIVVII.services.CamioneroService;
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
@RequestMapping("/rutas/usuarios/v1/camioneros")
public class CamioneroController {

    private final CamioneroService service;

    public CamioneroController(CamioneroService service) {
        this.service = service;
    }

    public record CamioneroBasicDto(Long id, String nombre, String apellidos, String rfc,
                                    String telefono, String direccion, java.math.BigDecimal salario) {
        public static CamioneroBasicDto from(Camionero c) {
            return new CamioneroBasicDto(
                    c.getId(), c.getNombre(), c.getApellidos(), c.getRfc(),
                    c.getTelefono(), c.getDireccion(), c.getSalario()
            );
        }
    }

    @Operation(
            summary = "Obtener listado de Camioneros",
            description = "Devuelve todos los camioneros registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Camioneros encontrados :D",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    [
                                      {
                                        "id": "int",
                                        "nombre": "String",
                                        "apellidos": "String",
                                        "rfc": "String",
                                        "telefono": "String",
                                        "direccion": "String",
                                        "salario": "Double"
                                      }
                                    ]
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
    @GetMapping
    public ResponseEntity<?> list() {
        try {
            List<CamioneroBasicDto> out = new ArrayList<>();
            service.getAllCamioneros().forEach(c -> out.add(CamioneroBasicDto.from(c)));
            return ResponseEntity.ok(out);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }

    @Operation(
            summary = "Obtener un Camionero por ID",
            description = "Devuelve la información de un camionero según su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Camionero encontrado :D",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "camionero": {
                                        "id": "int",
                                        "nombre": "String",
                                        "apellidos": "String",
                                        "rfc": "String",
                                        "telefono": "String",
                                        "direccion": "String",
                                        "salario": "Double"
                                      },
                                      "estado": 1,
                                      "mensaje": "Camionero encontrado :D"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Camionero no encontrado D:",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    { "estado": 0, "mensaje": "Camionero no encontrado D:" }
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
            Camionero camionero = service.getCamioneroById(id);
            if (camionero == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("estado", 0, "mensaje", "Camionero no encontrado D:"));
            }
            return ResponseEntity.ok(Map.of(
                    "estado", 1,
                    "mensaje", "Camionero encontrado :D",
                    "camionero", CamioneroBasicDto.from(camionero)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }

    @Operation(
            summary = "Crear un nuevo Camionero",
            description = "Crea un camionero con su información personal y de contacto.",
            requestBody = @RequestBody(
                    description = "Datos necesarios para registrar el camionero.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "nombre": "String",
                                      "apellidos": "String",
                                      "rfc": "String",
                                      "telefono": "String",
                                      "direccion": "String",
                                      "salario": "Double"
                                    }
                                    """)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Camionero creado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "id": "int",
                                      "nombre": "String",
                                      "apellidos": "String",
                                      "rfc": "String",
                                      "telefono": "String",
                                      "direccion": "String",
                                      "salario": "Double"
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
    public ResponseEntity<?> create(@Valid @RequestBody Camionero body) {
        try {
            Camionero saved = service.addCamionero(body);
            URI loc = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(saved.getId()).toUri();
            return ResponseEntity.created(loc).body(CamioneroBasicDto.from(saved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }

    @Operation(
            summary = "Actualizar un Camionero existente",
            description = "Modifica los datos de un camionero por su ID.",
            requestBody = @RequestBody(
                    description = "Datos para actualizar el camionero.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "nombre": "String",
                                      "apellidos": "String",
                                      "rfc": "String",
                                      "telefono": "String",
                                      "direccion": "String",
                                      "salario": "Double"
                                    }
                                    """)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Camionero actualizado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "id": "int",
                                      "nombre": "String",
                                      "apellidos": "String",
                                      "rfc": "String",
                                      "telefono": "String",
                                      "direccion": "String",
                                      "salario": "Double"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Camionero no encontrado D:",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    { "estado": 0, "mensaje": "Camionero no encontrado D:" }
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
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Camionero body) {
        try {
            Camionero updated = service.updateCamionero(id, body);
            return ResponseEntity.ok(CamioneroBasicDto.from(updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }

    @Operation(
            summary = "Eliminar un Camionero",
            description = "Elimina un camionero existente por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Camionero eliminado correctamente (sin contenido)"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Camionero no encontrado D:",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = """
                                    { "estado": 0, "mensaje": "Camionero no encontrado D:" }
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
            service.deleteCamionero(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }
}
