package com.upiiz.PracticaIIIIVVII.services;

import com.upiiz.PracticaIIIIVVII.models.Camionero;
import com.upiiz.PracticaIIIIVVII.repositories.CamioneroRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CamioneroService {

    private final CamioneroRepository camionerorepository;

    public CamioneroService(CamioneroRepository camionerorepository) { this.camionerorepository = camionerorepository; }

    public List<Camionero> getAllCamioneros() {return (List<Camionero>) camionerorepository.findAll(); }

    public Camionero getCamioneroById(Long id) { return camionerorepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Camionero no encontrado"));
    }

    public Camionero addCamionero(@Valid Camionero c) {
        c.setId(null);
        return camionerorepository.save(c);
    }

    public Camionero updateCamionero(Long id, @Valid Camionero camionero) { camionero.setId(id); return camionerorepository.save(camionero); }

    public void deleteCamionero(Long id) {
        if (!camionerorepository.existsById(Math.toIntExact(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Camionero no encontrado");
        }
        camionerorepository.deleteById(Math.toIntExact(id));
    }
}

