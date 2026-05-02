package com.canda.weatherapi.service;

import com.canda.weatherapi.client.GeoapifyClient;
import com.canda.weatherapi.client.OpenMeteoClient;
import com.canda.weatherapi.client.openmeteo.OpenMeteoResponse;
import com.canda.weatherapi.model.Location;
import com.canda.weatherapi.model.WeatherResponse;
import com.canda.weatherapi.mother.LocationMother;
import com.canda.weatherapi.mother.OpenMeteoResponseMother;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    private OpenMeteoClient openMeteoClient;

    @Mock
    private GeoapifyClient geoapifyClient;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    void getWeatherByAddress_delegatesProperly() {

        when(geoapifyClient.resolveAddress("Bucharest")).thenReturn(LocationMother.bucharest());
        when(openMeteoClient.fetchWeather(44.43, 26.1, 5)).thenReturn(OpenMeteoResponseMother.validResponse());

        WeatherResponse result = weatherService.getWeatherByAddress("Bucharest", 5);

        assertThat(result.getLocation().getName()).isEqualTo("Bucharest");
        assertThat(result.getCurrent().getTemperature()).isEqualTo(22.0);
    }

    @Test
    void getWeatherByCoordinates_valid_returnsWeatherResponse() {
        when(openMeteoClient.fetchWeather(52.52, 13.41, 5)).thenReturn(OpenMeteoResponseMother.validResponse());

        WeatherResponse result = weatherService.getWeatherByCoordinates(52.52, 13.41, 5, "Test Loc");

        assertThat(result.getLocation().getName()).isEqualTo("Test Loc");
        assertThat(result.getCurrent().getTemperature()).isEqualTo(22.0);
        assertThat(result.getCurrent().getCondition()).isEqualTo("Clear sky");
        assertThat(result.getForecast()).hasSize(1);
    }

    @Test
    void getWeatherByCoordinates_invalidLat_throwsException() {
        assertThatThrownBy(() -> weatherService.getWeatherByCoordinates(91.0, 13.41, 5, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
