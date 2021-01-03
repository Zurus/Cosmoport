package com.space.model;

public enum ShipType {
    TRANSPORT("TRANSPORT"),
    MILITARY("MILITARY"),
    MERCHANT("MERCHANT");

    private String fieldName;

    ShipType(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static ShipType getType(String fieldName) {
        for (ShipType shipType:ShipType.values()) {
            if(shipType.fieldName.equalsIgnoreCase(fieldName)){
                return shipType;
            }
        }
        return null;
    }
}