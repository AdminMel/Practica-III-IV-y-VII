package com.upiiz.PracticaIIIIVVII.controllers;

import com.upiiz.PracticaIIIIVVII.models.Camion;
import com.upiiz.PracticaIIIIVVII.services.CamionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rutas/usuarios/v1/camiones")
public class CamionController {

    private final CamionService service;

    public CamionController(CamionService service) { this.service = service; }

    // DTO básico (evita tocar colecciones LAZY)
    public record CamionBasicDto(Long id, String matricula, String modelo, String tipo) {
        public static CamionBasicDto from(Camion c) {
            return new CamionBasicDto(c.getId(), c.getMatricula(), c.getModelo(), c.getTipo());
        }
    }

    @GetMapping
    public List<CamionBasicDto> list() {
        List<CamionBasicDto> out = new ArrayList<>();
        service.getAllCamioness().forEach(c -> out.add(CamionBasicDto.from(c)));
        return out;
    }

    @GetMapping("/{id}")
    public CamionBasicDto get(@PathVariable Long id) {
        return CamionBasicDto.from(service.getCamionById(id));
    }

    @PostMapping
    public ResponseEntity<CamionBasicDto> create(@Valid @RequestBody Camion body) {
        Camion saved = service.addCamion(body);
        URI loc = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(loc).body(CamionBasicDto.from(saved));
    }

    @PutMapping("/{id}")
    public CamionBasicDto update(@PathVariable Long id, @Valid @RequestBody Camion body) {
        return CamionBasicDto.from(service.updateCamion(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCamion(id);
        return ResponseEntity.noContent().build();
    }
}
