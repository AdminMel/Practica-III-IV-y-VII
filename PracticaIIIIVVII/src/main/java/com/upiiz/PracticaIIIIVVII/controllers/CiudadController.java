package com.upiiz.PracticaIIIIVVII.controllers;

import com.upiiz.PracticaIIIIVVII.models.Ciudad;
import com.upiiz.PracticaIIIIVVII.services.CiudadService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rutas/usuarios/v1/ciudades")
public class CiudadController {

    private final CiudadService service;

    public CiudadController(CiudadService service) { this.service = service; }

    public record CiudadBasicDto(Long id, String codigo, String nombre) {
        public static CiudadBasicDto from(Ciudad c) {
            return new CiudadBasicDto(c.getId(), c.getCodigo(), c.getNombre());
        }
    }

    @GetMapping
    public List<CiudadBasicDto> list() {
        List<CiudadBasicDto> out = new ArrayList<>();
        service.getAllCiudades().forEach(c -> out.add(CiudadBasicDto.from(c)));
        return out;
    }

    @GetMapping("/{id}")
    public CiudadBasicDto get(@PathVariable Long id) {
        return CiudadBasicDto.from(service.getCiudadById(id));
    }

    @PostMapping
    public ResponseEntity<CiudadBasicDto> create(@Valid @RequestBody Ciudad body) {
        Ciudad saved = service.addCiudad(body);
        URI loc = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(loc).body(CiudadBasicDto.from(saved));
    }

    @PutMapping("/{id}")
    public CiudadBasicDto update(@PathVariable Long id, @Valid @RequestBody Ciudad body) {
        return CiudadBasicDto.from(service.updateCiudad(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCiudad(id);
        return ResponseEntity.noContent().build();
    }
}
