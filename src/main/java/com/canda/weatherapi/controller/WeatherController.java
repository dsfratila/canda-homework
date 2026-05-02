package com.canda.weatherapi.controller;

import com.canda.weatherapi.model.WeatherResponse;
import com.canda.weatherapi.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public ResponseEntity<WeatherResponse> getWeather(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon,
            @RequestParam(required = false, defaultValue = "5") Integer days) {

        if (days < 1 || days > 10) {
            throw new IllegalArgumentException("Days parameter must be between 1 and 10.");
        }

        if (address != null && !address.trim().isEmpty()) {
            return ResponseEntity.ok(weatherService.getWeatherByAddress(address, days));
        } else if (lat != null && lon != null) {
            return ResponseEntity.ok(weatherService.getWeatherByCoordinates(lat, lon, days, null));
        } else {
            throw new IllegalArgumentException("You must provide either an address or both lat and lon.");
        }
    }
}
