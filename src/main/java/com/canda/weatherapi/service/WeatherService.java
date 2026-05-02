package com.canda.weatherapi.service;

import com.canda.weatherapi.client.GeoapifyClient;
import com.canda.weatherapi.client.OpenMeteoClient;
import com.canda.weatherapi.client.openmeteo.OpenMeteoResponse;
import com.canda.weatherapi.model.CurrentWeather;
import com.canda.weatherapi.model.ForecastDay;
import com.canda.weatherapi.model.Location;
import com.canda.weatherapi.model.WeatherResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private final OpenMeteoClient openMeteoClient;
    private final GeoapifyClient geoapifyClient;

    public WeatherService(OpenMeteoClient openMeteoClient, GeoapifyClient geoapifyClient) {
        this.openMeteoClient = openMeteoClient;
        this.geoapifyClient = geoapifyClient;
    }

    public WeatherResponse getWeatherByCoordinates(Double lat, Double lon, Integer days, String locName) {
        validateCoordinates(lat, lon);
        
        Location location = Location.builder()
                .name(locName != null ? locName : "Unknown Location")
                .lat(lat)
                .lon(lon)
                .build();

        OpenMeteoResponse response = openMeteoClient.fetchWeather(lat, lon, days);

        return WeatherResponse.builder()
                .location(location)
                .current(mapCurrentWeather(response.getCurrent()))
                .forecast(mapForecastDays(response.getDaily()))
                .build();
    }

    public WeatherResponse getWeatherByAddress(String address, Integer days) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address must not be empty if provided.");
        }
        Location loc = geoapifyClient.resolveAddress(address);
        return getWeatherByCoordinates(loc.getLat(), loc.getLon(), days, loc.getName());
    }

    private void validateCoordinates(Double lat, Double lon) {
        if (lat == null || lon == null) {
            throw new IllegalArgumentException("Both lat and lon must be provided.");
        }
        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            throw new IllegalArgumentException("Invalid latitude or longitude.");
        }
    }

    private CurrentWeather mapCurrentWeather(OpenMeteoResponse.Current current) {
        if (current == null) return null;
        return CurrentWeather.builder()
                .temperature(current.getTemperature2m())
                .humidity(current.getRelativeHumidity2m())
                .windSpeed(current.getWindSpeed10m())
                .condition(mapWeatherCode(current.getWeatherCode()))
                .timestamp(current.getTime() + ":00Z")
                .build();
    }

    private List<ForecastDay> mapForecastDays(OpenMeteoResponse.Daily daily) {
        List<ForecastDay> forecast = new ArrayList<>();
        if (daily == null || daily.getTime() == null)
            return forecast;

        for (int i = 0; i < daily.getTime().size(); i++) {
            forecast.add(ForecastDay.builder()
                    .date(daily.getTime().get(i))
                    .minTemp(daily.getTemperature2mMin().get(i))
                    .maxTemp(daily.getTemperature2mMax().get(i))
                    .precipitationChance(daily.getPrecipitationProbabilityMax().get(i))
                    .condition(mapWeatherCode(daily.getWeatherCode().get(i)))
                    .build());
        }
        return forecast;
    }

    private String mapWeatherCode(Integer code) {
        if (code == null) return "Unknown";
        if (code == 0) return "Clear sky";
        if (code <= 3) return "Mainly clear, partly cloudy, or overcast";
        if (code == 45 || code == 48) return "Fog";
        if (code >= 51 && code <= 55) return "Drizzle";
        if (code >= 61 && code <= 65) return "Rain";
        if (code >= 71 && code <= 75) return "Snow";
        if (code >= 80 && code <= 82) return "Rain showers";
        if (code >= 85 && code <= 86) return "Snow showers";
        if (code >= 95 && code <= 99) return "Thunderstorm";
        return "Unknown";
    }
}
