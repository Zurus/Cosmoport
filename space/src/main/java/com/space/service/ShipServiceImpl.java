package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.ShipType;
import com.space.model.SpaceShip;
import com.space.repository.SpaceShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ShipServiceImpl implements SpaceShipService {

    private SpaceShipRepository spaceShipRepository;
    private SpaceShipUtils spaceShipUtils;

    public ShipServiceImpl() {
    }

    @Autowired
    public ShipServiceImpl(SpaceShipRepository shipRepository, SpaceShipUtils spaceShipUtils) {
        this.spaceShipRepository = shipRepository;
        this.spaceShipUtils = spaceShipUtils;
    }

    @Override
    public SpaceShip saveShip(SpaceShip ship) {
        return spaceShipRepository.save(ship);
    }

    @Override
    public SpaceShip getShip(Long id) {
        return spaceShipRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteShip(SpaceShip ship) {
        spaceShipRepository.delete(ship);
    }

//    @Override
//    @Transactional
//    public List<SpaceShip> getAllShips(Map<String, Object> params) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        javax.persistence.criteria.CriteriaQuery<SpaceShip> criteriaQuery = cb.createQuery(SpaceShip.class);
//        Root<SpaceShip> contactEntityRoot = criteriaQuery.from(SpaceShip.class);
//        //contactEntityRoot.fetch(SpaceShip.contactTelDetails, JoinType.LEFT);
//        //contactEntityRoot.fetch(SpaceShip.hobbies, JoinType.LEFT);
//
//        criteriaQuery.select(contactEntityRoot).distinct(true);
//
//        Predicate criteria = cb.conjunction();
//        //name
//        if (params.get(FilterQuery.NAME) != null) {
//            Predicate p = cb.like(contactEntityRoot.get("name"), (String)params.get(FilterQuery.NAME) );
//            criteria = cb.and(criteria, p);
//        }
//        //planet
//        if (params.get(FilterQuery.PLANET) != null) {
//            Predicate p = cb.like(contactEntityRoot.get("planet"), (String)params.get(FilterQuery.PLANET));
//            criteria = cb.and(criteria, p);
//        }
//        //shipType
//        if (params.get(FilterQuery.SHIP_TYPE) != null) {
//            Predicate p = cb.equal(contactEntityRoot.get("shipType"), ShipType.getType((String)params.get(FilterQuery.SHIP_TYPE)));
//            criteria = cb.and(criteria, p);
//        }
//        //after
//        if (params.get(FilterQuery.AFTER) != null) {
//            Predicate pAfter = cb.greaterThanOrEqualTo(contactEntityRoot.get("prodDate"), getCurrentDate((String)params.get(FilterQuery.AFTER)));
//            criteria = cb.and(criteria, pAfter);
//        }
//        //before
//        if (params.get(FilterQuery.BEFORE) != null) {
//            Predicate pBefore = cb.greaterThanOrEqualTo(contactEntityRoot.get("prodDate"), getCurrentDate((String)params.get(FilterQuery.BEFORE)));
//            criteria = cb.and(criteria, pBefore);
//        }
//
//        //crew size between
//        if (params.get(FilterQuery.MIN_CREW_SIZE) != null) {
//            Predicate pBefore = cb.greaterThanOrEqualTo(contactEntityRoot.get("crewSize"), Integer.valueOf((String)params.get(FilterQuery.MIN_CREW_SIZE)));
//            criteria = cb.and(criteria, pBefore);
//        }
//
//        //crew size between
//        if (params.get(FilterQuery.MAX_CREW_SIZE) != null) {
//            Predicate pBefore = cb.lessThanOrEqualTo(contactEntityRoot.get("crewSize"), Integer.valueOf((String)params.get(FilterQuery.MAX_CREW_SIZE)));
//            criteria = cb.and(criteria, pBefore);
//        }
//
//        //speed
//        if (params.get(FilterQuery.MIN_SPEED) != null) {
//            Predicate pBefore = cb.greaterThanOrEqualTo(contactEntityRoot.get("speed"), Double.valueOf((String)params.get(FilterQuery.MIN_SPEED)));
//            criteria = cb.and(criteria, pBefore);
//        }
//        //speed
//        if (params.get(FilterQuery.MAX_SPEED) != null) {
//            Predicate pBefore = cb.lessThanOrEqualTo(contactEntityRoot.get("speed"), Double.valueOf((String)params.get(FilterQuery.MAX_SPEED)));
//            criteria = cb.and(criteria, pBefore);
//        }
//
//        //rating
//        if (params.get(FilterQuery.MIN_RATING) != null) {
//            Predicate pBefore = cb.greaterThanOrEqualTo(contactEntityRoot.get("rating"), Double.valueOf((String)params.get(FilterQuery.MIN_RATING)));
//            criteria = cb.and(criteria, pBefore);
//        }
//
//        //rating
//        if (params.get(FilterQuery.MAX_RATING) != null) {
//            Predicate pBefore = cb.lessThanOrEqualTo(contactEntityRoot.get("rating"), Double.valueOf((String)params.get(FilterQuery.MAX_RATING)));
//            criteria = cb.and(criteria, pBefore);
//        }
//
//        if (params.get(FilterQuery.IS_USED) != null) {
//            Predicate p = cb.equal(contactEntityRoot.get("isUsed"), Boolean.valueOf((String)params.get(FilterQuery.IS_USED)));
//            criteria = cb.and(criteria, p);
//        }
//
//
//        criteriaQuery.where(criteria);
//        List<SpaceShip> result = em.createQuery(criteriaQuery).getResultList();
//        return result;
//    }

    @Override
    public List<SpaceShip> getAllShips(Map<String, Object> params) {
        final Date afterDate = params.get(FilterQuery.AFTER) == null ? null : new Date(Long.parseLong((String)params.get(FilterQuery.AFTER)));
        final Date beforeDate = params.get(FilterQuery.BEFORE) == null ? null : new Date(Long.parseLong((String)params.get(FilterQuery.BEFORE)));
        final List<SpaceShip> list = new ArrayList<>();
        spaceShipRepository.findAll().forEach((ship) -> {
            if (params.get(FilterQuery.NAME) != null && !ship.getName().contains((String)params.get(FilterQuery.NAME))) return;
            if (params.get(FilterQuery.PLANET) != null && !ship.getPlanet().contains((String)params.get(FilterQuery.PLANET))) return;
            if (params.get(FilterQuery.SHIP_TYPE) != null && ship.getShipType() != ShipType.getType((String)params.get(FilterQuery.SHIP_TYPE))) return;
            if (afterDate != null && ship.getProdDate().before(afterDate)) return;
            if (beforeDate != null && ship.getProdDate().after(beforeDate)) return;
            if (params.get(FilterQuery.IS_USED) != null && ship.getUsed() != Boolean.valueOf((String)params.get(FilterQuery.IS_USED))) return;
            if (params.get(FilterQuery.MIN_SPEED) != null && Double.valueOf(ship.getSpeed()).compareTo(Double.valueOf((String)params.get(FilterQuery.MIN_SPEED))) < 0) return;
            if (params.get(FilterQuery.MAX_SPEED) != null && Double.valueOf(ship.getSpeed()).compareTo(Double.valueOf((String)params.get(FilterQuery.MAX_SPEED))) > 0) return;
            if (params.get(FilterQuery.MIN_CREW_SIZE) != null && Integer.valueOf(ship.getCrewSize()).compareTo(Integer.valueOf((String)params.get(FilterQuery.MIN_CREW_SIZE))) < 0) return;
            if (params.get(FilterQuery.MAX_CREW_SIZE) != null && Integer.valueOf(ship.getCrewSize()).compareTo(Integer.valueOf((String)params.get(FilterQuery.MAX_CREW_SIZE))) > 0) return;
            if (params.get(FilterQuery.MIN_RATING) != null && Double.valueOf(ship.getRating()).compareTo(Double.valueOf((String)params.get(FilterQuery.MIN_RATING))) < 0) return;
            if (params.get(FilterQuery.MAX_RATING) != null && Double.valueOf(ship.getRating()).compareTo(Double.valueOf((String)params.get(FilterQuery.MAX_RATING))) > 0) return;

            list.add(ship);
        });
        return list;
    }

    @Override
    public List<SpaceShip> sortShips(List<SpaceShip> ships, ShipOrder order) {
        if (order != null) {
            ships.sort((ship1, ship2) -> {
                switch (order) {
                    case ID: return ship1.getId().compareTo(ship2.getId());
                    case SPEED: return ship1.getSpeed().compareTo(ship2.getSpeed());
                    case DATE: return ship1.getProdDate().compareTo(ship2.getProdDate());
                    case RATING: return ship1.getRating().compareTo(ship2.getRating());
                    default: return 0;
                }
            });
        }
        return ships;
    }

    @Override
    public List<SpaceShip> getPage(List<SpaceShip> ships, Integer pageNumber, Integer pageSize) {
        final Integer page = pageNumber == null ? 0 : pageNumber;
        final Integer size = pageSize == null ? 3 : pageSize;
        final int from = page * size;
        int to = from + size;
        if (to > ships.size()) to = ships.size();
        return ships.subList(from, to);
    }

    @Override
    public boolean isShipValid(SpaceShip ship) {
        return ship != null && spaceShipUtils.isStringValid(ship.getName()) && spaceShipUtils.isStringValid(ship.getPlanet())
                && spaceShipUtils.isProdDateValid(ship.getProdDate())
                && spaceShipUtils.isSpeedValid(ship.getSpeed())
                && spaceShipUtils.isCrewSizeValid(ship.getCrewSize());
    }

    @Override
    public SpaceShip updateShip(SpaceShip oldShip, SpaceShip newShip) throws IllegalArgumentException {
        boolean shouldChangeRating = false;

        final String name = newShip.getName();
        if (name != null) {
            if (spaceShipUtils.isStringValid(name)) {
                oldShip.setName(name);
            } else {
                throw new IllegalArgumentException();
            }
        }
        final String planet = newShip.getPlanet();
        if (planet != null) {
            if (spaceShipUtils.isStringValid(planet)) {
                oldShip.setPlanet(planet);
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (newShip.getShipType() != null) {
            oldShip.setShipType(newShip.getShipType());
        }
        final Date prodDate = newShip.getProdDate();
        if (prodDate != null) {
            if (spaceShipUtils.isProdDateValid(prodDate)) {
                oldShip.setProdDate(prodDate);
                shouldChangeRating = true;
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (newShip.getUsed() != null) {
            oldShip.setUsed(newShip.getUsed());
            shouldChangeRating = true;
        }
        final Double speed = newShip.getSpeed();
        if (speed != null) {
            if (spaceShipUtils.isSpeedValid(speed)) {
                oldShip.setSpeed(speed);
                shouldChangeRating = true;
            } else {
                throw new IllegalArgumentException();
            }
        }
        final Integer crewSize = newShip.getCrewSize();
        if (crewSize != null) {
            if (spaceShipUtils.isCrewSizeValid(crewSize)) {
                oldShip.setCrewSize(crewSize);
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (shouldChangeRating) {
            final double rating = spaceShipUtils.computeRating(oldShip.getSpeed(), oldShip.getUsed(), oldShip.getProdDate());
            oldShip.setRating(rating);
        }
        spaceShipRepository.save(oldShip);
        return oldShip;
    }
}