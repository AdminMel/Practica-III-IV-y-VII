package com.upiiz.PracticaIIIIVVII.services;

import com.upiiz.PracticaIIIIVVII.models.Camion;
import com.upiiz.PracticaIIIIVVII.repositories.CamionRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CamionService {

    private final CamionRepository camionrepository;

    public CamionService(CamionRepository camionrepository) { this.camionrepository = camionrepository; }

    public List<Camion> getAllCamioness() { return (List<Camion>) camionrepository.findAll(); }

    public Camion getCamionById(Long id) {
        return camionrepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Camión no encontrado"));
    }

    public Camion addCamion(@Valid Camion camion) {
        camion.setId(null);
        return camionrepository.save(camion);
    }

    public Camion updateCamion(Long id, @Valid Camion camion) { camion.setId(id); return camionrepository.save(camion); }

    public void deleteCamion(Long id) {
        if (!camionrepository.existsById(Math.toIntExact(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Camión no encontrado");
        }
        camionrepository.deleteById(Math.toIntExact(id));
    }
}

