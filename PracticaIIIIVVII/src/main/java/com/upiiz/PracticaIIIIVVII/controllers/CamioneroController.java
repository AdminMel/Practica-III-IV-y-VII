package com.upiiz.PracticaIIIIVVII.controllers;

import com.upiiz.PracticaIIIIVVII.models.Camionero;
import com.upiiz.PracticaIIIIVVII.services.CamioneroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rutas/usuarios/v1/camioneros")
public class CamioneroController {

    private final CamioneroService service;

    public CamioneroController(CamioneroService service) { this.service = service; }

    public record CamioneroBasicDto(Long id, String nombre, String apellidos, String rfc) {
        public static CamioneroBasicDto from(Camionero c) {
            return new CamioneroBasicDto(c.getId(), c.getNombre(), c.getApellidos(), c.getRfc());
        }
    }

    @GetMapping
    public List<CamioneroBasicDto> list() {
        List<CamioneroBasicDto> out = new ArrayList<>();
        service.getAllCamioneros().forEach(c -> out.add(CamioneroBasicDto.from(c)));
        return out;
    }

    @GetMapping("/{id}")
    public CamioneroBasicDto get(@PathVariable Long id) {
        return CamioneroBasicDto.from(service.getCamioneroById(id));
    }

    @PostMapping
    public ResponseEntity<CamioneroBasicDto> create(@Valid @RequestBody Camionero body) {
        Camionero saved = service.addCamionero(body);
        URI loc = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(loc).body(CamioneroBasicDto.from(saved));
    }

    @PutMapping("/{id}")
    public CamioneroBasicDto update(@PathVariable Long id, @Valid @RequestBody Camionero body) {
        return CamioneroBasicDto.from(service.updateCamionero(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCamionero(id);
        return ResponseEntity.noContent().build();
    }
}
