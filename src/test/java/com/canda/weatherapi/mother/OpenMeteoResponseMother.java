package com.canda.weatherapi.mother;

import com.canda.weatherapi.client.openmeteo.OpenMeteoResponse;

import java.util.List;

public class OpenMeteoResponseMother {

    public static OpenMeteoResponse validResponse() {
        OpenMeteoResponse mockResponse = new OpenMeteoResponse();
        
        OpenMeteoResponse.Current current = new OpenMeteoResponse.Current();
        current.setTime("2026-05-01T16:00");
        current.setTemperature2m(22.0);
        current.setWeatherCode(0);
        mockResponse.setCurrent(current);

        OpenMeteoResponse.Daily daily = new OpenMeteoResponse.Daily();
        daily.setTime(List.of("2026-05-01"));
        daily.setTemperature2mMax(List.of(25.0));
        daily.setTemperature2mMin(List.of(15.0));
        daily.setPrecipitationProbabilityMax(List.of(10.0));
        daily.setWeatherCode(List.of(0));
        mockResponse.setDaily(daily);
        
        return mockResponse;
    }
}
