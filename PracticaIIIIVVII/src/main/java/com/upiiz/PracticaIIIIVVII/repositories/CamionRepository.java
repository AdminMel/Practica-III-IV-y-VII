package com.upiiz.PracticaIIIIVVII.repositories;

import com.upiiz.PracticaIIIIVVII.models.Camion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CamionRepository extends CrudRepository<Camion, Integer> {
}
