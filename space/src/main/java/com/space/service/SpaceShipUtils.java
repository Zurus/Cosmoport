package com.space.service;

import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class SpaceShipUtils {
    public SpaceShipUtils() {
    }

    public double computeRating(double speed, boolean isUsed, Date prod) {
        final int now = 3019;
        final int prodYear = getYearFromDate(prod);
        final double k = isUsed ? 0.5 : 1;
        final double rating = 80 * speed * k / (now - prodYear + 1);
        return round(rating);
    }

    private Date getDateForYear(int year) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    private int getYearFromDate(Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    private double round(double value) {
        return Math.round(value * 100) / 100D;
    }

    public boolean isCrewSizeValid(Integer crewSize) {
        final int minCrewSize = 1;
        final int maxCrewSize = 9999;
        return crewSize != null && crewSize.compareTo(minCrewSize) >= 0 && crewSize.compareTo(maxCrewSize) <= 0;
    }

    public boolean isSpeedValid(Double speed) {
        final double minSpeed = 0.01;
        final double maxSpeed = 0.99;
        return speed != null && speed.compareTo(minSpeed) >= 0 && speed.compareTo(maxSpeed) <= 0;
    }

    public boolean isStringValid(String value) {
        final int maxStringLength = 50;
        return value != null && !value.isEmpty() && value.length() <= maxStringLength;
    }

    public boolean isProdDateValid(Date prodDate) {
        final Date startProd = getDateForYear(2800);
        final Date endProd = getDateForYear(3019);
        return prodDate != null && prodDate.after(startProd) && prodDate.before(endProd);
    }

}
