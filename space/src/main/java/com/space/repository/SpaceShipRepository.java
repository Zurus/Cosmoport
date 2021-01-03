package com.space.repository;

import com.space.model.SpaceShip;
import org.springframework.data.repository.CrudRepository;

public interface SpaceShipRepository extends CrudRepository<SpaceShip, Long> {
    void deleteById(Long id);
    SpaceShip getById(Long id);
    @Override
    void delete(SpaceShip entity);
}
