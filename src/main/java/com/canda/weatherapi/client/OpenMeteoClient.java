package com.canda.weatherapi.client;

import com.canda.weatherapi.client.openmeteo.OpenMeteoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OpenMeteoClient {

    private final RestClient restClient;
    private final String baseUrl;

    public OpenMeteoClient(RestClient restClient,
                           @Value("${weather.api.open-meteo.base-url}") String baseUrl) {
        this.restClient = restClient;
        this.baseUrl = baseUrl;
    }

    @Cacheable(value = "weatherCache", key = "#lat + '_' + #lon + '_' + #days")
    public OpenMeteoResponse fetchWeather(Double lat, Double lon, Integer days) {
        String uri = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/forecast")
                .queryParam("latitude", lat)
                .queryParam("longitude", lon)
                .queryParam("current", "temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code")
                .queryParam("daily", "weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max")
                .queryParam("forecast_days", days)
                .queryParam("timezone", "UTC")
                .build().toUriString();

        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(OpenMeteoResponse.class);
    }
}
