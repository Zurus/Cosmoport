package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.SpaceShip;

import java.util.List;
import java.util.Map;

public interface SpaceShipService {

    SpaceShip saveShip(SpaceShip ship);

    SpaceShip getShip(Long id);

    SpaceShip updateShip(SpaceShip oldShip, SpaceShip newShip) throws IllegalArgumentException;

    void deleteShip(SpaceShip ship);

    List<SpaceShip> getAllShips(
            Map<String, Object> params
    );

    List<SpaceShip> sortShips(List<SpaceShip> ships, ShipOrder order);

    List<SpaceShip> getPage(List<SpaceShip> ships, Integer pageNumber, Integer pageSize);

    boolean isShipValid(SpaceShip ship);
}