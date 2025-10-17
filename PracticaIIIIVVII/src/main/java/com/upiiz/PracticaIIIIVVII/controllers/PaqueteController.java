package com.upiiz.PracticaIIIIVVII.controllers;

import com.upiiz.PracticaIIIIVVII.models.Paquete;
import com.upiiz.PracticaIIIIVVII.services.PaqueteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/rutas/usuarios/v1/paquetes")
public class PaqueteController {

    private final PaqueteService service;

    public PaqueteController(PaqueteService service) {
        this.service = service;
    }

    public record PaqueteBasicDto(Long id, String codigo, String destinatario,
                                  Long camioneroId, Long ciudadId) {
        public static PaqueteBasicDto from(Paquete p) {
            Long camId = (p.getCamionero() != null) ? p.getCamionero().getId() : null;
            Long ciuId = (p.getCiudadDestino() != null) ? p.getCiudadDestino().getId() : null;
            return new PaqueteBasicDto(p.getId(), p.getCodigo(), p.getDestinatario(), camId, ciuId);
        }
    }

    public static class PaqueteDTO {
        @NotBlank @Size(max = 30)   public String codigo;
        @Size(max = 300)            public String descripcion;
        @Size(max = 150)            public String destinatario;
        @Size(max = 200)            public String direccion;
        @NotNull                    public Long camioneroId;   // obligatorio
        @NotNull                    public Long ciudadId;      // obligatorio
    }

    @Operation(
            summary = "Obtener el listado de Paquetes",
            description = "Regresa el listado de paquetes (forma básica)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Paquetes encontrados :D",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            [
                                              {
                                                "id": "int",
                                                "codigo": "String",
                                                "destinatario": "String",
                                                "camioneroId": "int",
                                                "ciudadId": "int"
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
                                            {
                                              "estado": "int",
                                              "mensaje": "String"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<?> list() {
        try {
            List<PaqueteBasicDto> out = new ArrayList<>();
            service.getAllPaquetes().forEach(p -> out.add(PaqueteBasicDto.from(p)));
            return ResponseEntity.ok(out);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }
    @Operation(
            summary = "Obtener un paquete por ID",
            description = "Regresa la información de un paquete específico según su identificador único (ID)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Paquete encontrado :D",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            {
                                              "paquete": {
                                                "id": "int",
                                                "codigo": "String",
                                                "destinatario": "String",
                                                "camioneroId": "int",
                                                "ciudadId": "int"
                                              },
                                              "estado": "int",
                                              "mensaje": "String"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Paquete no encontrado D:",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            {
                                              "estado": "int",
                                              "mensaje": "String"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Falta el ID o el formato es inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            {
                                              "estado": "int",
                                              "mensaje": "String"
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
                                            {
                                              "estado": "int",
                                              "mensaje": "String"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(
                        Map.of("estado", 0, "mensaje", "Falta el ID o el formato es inválido")
                );
            }
            Paquete modelo = service.getPaqueteById(id);
            if (modelo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        Map.of("estado", 0, "mensaje", "Paquete no encontrado D:")
                );
            }
            PaqueteBasicDto paquete = PaqueteBasicDto.from(modelo);
            return ResponseEntity.ok(
                    Map.of("estado", 1, "mensaje", "Paquete encontrado :D", "paquete", paquete)
            );
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }

    @Operation(
            summary = "Crear un paquete",
            description = "Crea un nuevo paquete con sus relaciones (camionero y ciudad)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Paquete creado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            {
                                              "id": "int",
                                              "codigo": "String",
                                              "destinatario": "String",
                                              "camioneroId": "int",
                                              "ciudadId": "int"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos o faltantes",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            {
                                              "estado": "int",
                                              "mensaje": "String",
                                              "errores": {
                                                "codigo": "String",
                                                "camioneroId": "String",
                                                "ciudadId": "String"
                                              }
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
                                            {
                                              "estado": "int",
                                              "mensaje": "String"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody PaqueteDTO dto) {
        try {
            Paquete data = new Paquete();
            data.setCodigo(dto.codigo);
            data.setDescripcion(dto.descripcion);
            data.setDestinatario(dto.destinatario);
            data.setDireccion(dto.direccion);

            Paquete saved = service.addPaquete(data, dto.camioneroId, dto.ciudadId);

            URI loc = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(saved.getId()).toUri();
            return ResponseEntity.created(loc).body(PaqueteBasicDto.from(saved));
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }

    @Operation(
            summary = "Actualizar un paquete",
            description = "Actualiza un paquete existente por ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Paquete actualizado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            {
                                              "id": "int",
                                              "codigo": "String",
                                              "destinatario": "String",
                                              "camioneroId": "int",
                                              "ciudadId": "int"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            {
                                              "estado": "int",
                                              "mensaje": "String",
                                              "errores": {
                                                "codigo": "String"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Paquete no encontrado D:",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            {
                                              "estado": "int",
                                              "mensaje": "String"
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
                                            {
                                              "estado": "int",
                                              "mensaje": "String"
                                            }
                                            """
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody PaqueteDTO dto) {
        try {
            Paquete data = new Paquete();
            data.setCodigo(dto.codigo);
            data.setDescripcion(dto.descripcion);
            data.setDestinatario(dto.destinatario);
            data.setDireccion(dto.direccion);

            Paquete updated = service.updatePaquete(id, data, dto.camioneroId, dto.ciudadId);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("estado", 0, "mensaje", "Paquete no encontrado D:"));
            }
            return ResponseEntity.ok(PaqueteBasicDto.from(updated));
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }

    @Operation(
            summary = "Eliminar un paquete",
            description = "Elimina un paquete existente por ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Eliminado correctamente (sin contenido)"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Paquete no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                                            {
                                              "estado": "int",
                                              "mensaje": "String"
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
                                            {
                                              "estado": "int",
                                              "mensaje": "String"
                                            }
                                            """
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deletePaquete(id); // lanza 404 si no existe
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("estado", 0, "mensaje", "Error interno del servidor D:"));
        }
    }
}
