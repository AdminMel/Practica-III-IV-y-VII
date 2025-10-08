package com.upiiz.PracticaIIIIVVII.services;

import com.upiiz.PracticaIIIIVVII.models.*;
import com.upiiz.PracticaIIIIVVII.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaqueteService {

    private final PaqueteRepository repo;
    private final CamioneroRepository camRepo;
    private final CiudadRepository ciudadRepo;

    public PaqueteService(PaqueteRepository repo,
                          CamioneroRepository camRepo,
                          CiudadRepository ciudadRepo) {
        this.repo = repo;
        this.camRepo = camRepo;
        this.ciudadRepo = ciudadRepo;
    }

    public List<Paquete> getAllPaquetes() { return (List<Paquete>) repo.findAll(); }

    public Paquete getPaqueteById(Long id) {
        return repo.findById(Math.toIntExact(id)).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Paquete no encontrado"));
    }

    public Paquete addPaquete(Paquete p, Long camioneroId, Long ciudadId) {
        Camionero cam = camRepo.findById(Math.toIntExact(camioneroId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Camionero no existe"));
        Ciudad ciu = ciudadRepo.findById(Math.toIntExact(ciudadId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no existe"));

        p.setId(null);
        p.setCamionero(cam);
        p.setCiudadDestino(ciu);
        return repo.save(p);
    }

    public Paquete updatePaquete(Long id, Paquete paquete, Long camioneroId, Long ciudadId) {
        Camionero cam = camRepo.findById(Math.toIntExact(camioneroId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Camionero no existe"));
        Ciudad ciu = ciudadRepo.findById(Math.toIntExact(ciudadId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no existe"));

        paquete.setCamionero(cam);
        paquete.setCiudadDestino(ciu);
        return repo.save(paquete);
    }

    public void deletePaquete(Long id) {
        if (!repo.existsById(Math.toIntExact(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paquete no encontrado");
        }
        repo.deleteById(Math.toIntExact(id));
    }
}
