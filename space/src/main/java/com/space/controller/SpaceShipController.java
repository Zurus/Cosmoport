package com.space.controller;

import com.space.model.SpaceShip;
import com.space.service.SpaceShipService;
import com.space.service.SpaceShipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class SpaceShipController {

    private SpaceShipService spaceShipService;
    private SpaceShipUtils spaceShipUtils;

    public SpaceShipController() {
    }

    @Autowired
    public SpaceShipController(SpaceShipService shipService, SpaceShipUtils spaceShipUtils) {
        this.spaceShipService = shipService;
        this.spaceShipUtils = spaceShipUtils;
    }

    @GetMapping(path = "/rest/ships")
    public List<SpaceShip> getAllShips(
            @RequestParam Map<String, Object> params,
            @RequestParam(value = "order", required = false) ShipOrder order,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        final List<SpaceShip> ships = spaceShipService.getAllShips(params);

        final List<SpaceShip> sortedShips = spaceShipService.sortShips(ships, order);

        return spaceShipService.getPage(sortedShips, pageNumber, pageSize);
    }

    @GetMapping(path = "/rest/ships/count")
    public Integer getShipsCount(
            @RequestParam Map<String, Object> params
    ) {
        return spaceShipService.getAllShips(params).size();
    }

    @PostMapping(path = "/rest/ships")
    @ResponseBody
    public ResponseEntity<SpaceShip> createShip(@RequestBody SpaceShip ship) {
        if (!spaceShipService.isShipValid(ship)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (ship.getUsed() == null) ship.setUsed(false);
        final double rating = spaceShipUtils.computeRating(ship.getSpeed(), ship.getUsed(), ship.getProdDate());
        ship.setRating(rating);
        final SpaceShip savedShip = spaceShipService.saveShip(ship);
        return new ResponseEntity<>(savedShip, HttpStatus.OK);
    }

    @GetMapping(path = "/rest/ships/{id}")
    public ResponseEntity<SpaceShip> getShip(@PathVariable(value = "id") String pathId) {
        final Long id = parseId(pathId);
        if (id == null || id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final SpaceShip ship = spaceShipService.getShip(id);
        if (ship == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @PostMapping(path = "/rest/ships/{id}")
    @ResponseBody
    public ResponseEntity<SpaceShip> updateShip(
            @PathVariable(value = "id") String pathId,
            @RequestBody SpaceShip ship
    ) {
        final ResponseEntity<SpaceShip> entity = getShip(pathId);
        final SpaceShip savedShip = entity.getBody();
        if (savedShip == null) {
            return entity;
        }
        try {
            SpaceShip result = spaceShipService.updateShip(savedShip, ship);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/rest/ships/{id}")
    public ResponseEntity<SpaceShip> deleteShip(@PathVariable(value = "id") String pathId) {
        final ResponseEntity<SpaceShip> entity = getShip(pathId);
        final SpaceShip savedShip = entity.getBody();
        if (savedShip == null) {
            return entity;
        }
        spaceShipService.deleteShip(savedShip);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Long parseId(String pathId) {
        if (pathId == null) {
            return null;
        } else try {
            return Long.parseLong(pathId);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}