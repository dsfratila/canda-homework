package com.canda.weatherapi.mother;

import com.canda.weatherapi.model.Location;

public class LocationMother {

    public static Location bucharest() {
        return Location.builder()
                .name("Bucharest")
                .lat(44.43)
                .lon(26.1)
                .build();
    }

    public static Location unknownGeoLocation() {
        return Location.builder()
                .name("Unknown Location")
                .lat(52.52)
                .lon(13.41)
                .build();
    }
}
