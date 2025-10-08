package com.upiiz.PracticaIIIIVVII.repositories;

import com.upiiz.PracticaIIIIVVII.models.Ciudad;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CiudadRepository extends CrudRepository<Ciudad, Integer> {
}
