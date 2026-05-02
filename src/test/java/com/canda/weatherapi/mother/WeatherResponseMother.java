package com.canda.weatherapi.mother;

import com.canda.weatherapi.model.WeatherResponse;

public class WeatherResponseMother {

    public static WeatherResponse bucharest() {
        return WeatherResponse.builder()
                .location(LocationMother.bucharest())
                .build();
    }

    public static WeatherResponse unknownGeoLocation() {
        return WeatherResponse.builder()
                .location(LocationMother.unknownGeoLocation())
                .build();
    }
}
