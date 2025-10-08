package com.upiiz.PracticaIIIIVVII.controllers;

import com.upiiz.PracticaIIIIVVII.models.Paquete;
import com.upiiz.PracticaIIIIVVII.services.PaqueteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rutas/usuarios/v1/paquetes")
public class PaqueteController {

    private final PaqueteService service;

    public PaqueteController(PaqueteService service) { this.service = service; }

    // -------- DTOs
    public record PaqueteBasicDto(Long id, String codigo, String destinatario,
                                  Long camioneroId, Long ciudadId) {
        public static PaqueteBasicDto from(Paquete p) {
            Long camId = (p.getCamionero() != null) ? p.getCamionero().getId() : null;
            Long ciuId = (p.getCiudadDestino() != null) ? p.getCiudadDestino().getId() : null;
            return new PaqueteBasicDto(p.getId(), p.getCodigo(), p.getDestinatario(), camId, ciuId);
        }
    }

    public static class PaqueteDTO {
        @NotBlank @Size(max = 30)  public String codigo;
        @Size(max = 300)           public String descripcion;
        @Size(max = 150)           public String destinatario;
        @Size(max = 200)           public String direccion;
        public Long camioneroId;   // obligatorio
        public Long ciudadId;      // obligatorio
    }

    @GetMapping
    public List<PaqueteBasicDto> list() {
        List<PaqueteBasicDto> out = new ArrayList<>();
        service.getAllPaquetes().forEach(p -> out.add(PaqueteBasicDto.from(p)));
        return out;
    }

    @GetMapping("/{id}")
    public PaqueteBasicDto get(@PathVariable Long id) {
        return PaqueteBasicDto.from(service.getPaqueteById(id));
    }

    @PostMapping
    public ResponseEntity<PaqueteBasicDto> create(@Valid @RequestBody PaqueteDTO dto) {
        Paquete data = new Paquete();
        data.setCodigo(dto.codigo);
        data.setDescripcion(dto.descripcion);
        data.setDestinatario(dto.destinatario);
        data.setDireccion(dto.direccion);

        Paquete saved = service.addPaquete(data, dto.camioneroId, dto.ciudadId);

        URI loc = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(loc).body(PaqueteBasicDto.from(saved));
    }

    @PutMapping("/{id}")
    public PaqueteBasicDto update(@PathVariable Long id, @Valid @RequestBody PaqueteDTO dto) {
        Paquete data = new Paquete();
        data.setCodigo(dto.codigo);
        data.setDescripcion(dto.descripcion);
        data.setDestinatario(dto.destinatario);
        data.setDireccion(dto.direccion);
        return PaqueteBasicDto.from(service.updatePaquete(id, data, dto.camioneroId, dto.ciudadId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletePaquete(id);
        return ResponseEntity.noContent().build();
    }
}
