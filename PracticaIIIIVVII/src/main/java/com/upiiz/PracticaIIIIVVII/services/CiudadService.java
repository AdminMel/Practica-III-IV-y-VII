package com.upiiz.PracticaIIIIVVII.services;

import com.upiiz.PracticaIIIIVVII.models.Ciudad;
import com.upiiz.PracticaIIIIVVII.repositories.CiudadRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CiudadService {

    private final CiudadRepository repo;

    public CiudadService(CiudadRepository repo) { this.repo = repo; }

    public List<Ciudad> getAllCiudades() { return (List<Ciudad>) repo.findAll(); }

    public Ciudad getCiudadById(Long id) {
        return repo.findById(Math.toIntExact(id)).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada"));
    }

    public Ciudad addCiudad(Ciudad c) {
        c.setId(null);
        return repo.save(c);
    }

    public Ciudad updateCiudad(Long id, Ciudad ciudad) { ciudad.setId(id); return repo.save(ciudad); }

    public void deleteCiudad(Long id) {
        if (!repo.existsById(Math.toIntExact(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada");
        }
        try {
            repo.deleteById(Math.toIntExact(id));
        } catch (DataIntegrityViolationException e) {
            // FK desde paquete -> ciudad
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede eliminar: hay paquetes con destino en esta ciudad");
        }
    }
}
