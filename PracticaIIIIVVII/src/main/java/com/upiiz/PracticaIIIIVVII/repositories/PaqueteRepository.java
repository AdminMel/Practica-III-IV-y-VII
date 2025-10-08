package com.upiiz.PracticaIIIIVVII.repositories;

import com.upiiz.PracticaIIIIVVII.models.Paquete;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaqueteRepository extends CrudRepository<Paquete, Integer> {
}
