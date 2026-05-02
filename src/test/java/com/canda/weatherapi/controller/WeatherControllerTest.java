package com.canda.weatherapi.controller;

import com.canda.weatherapi.model.WeatherResponse;
import com.canda.weatherapi.mother.WeatherResponseMother;
import com.canda.weatherapi.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherService weatherService;

    @Test
    void getWeatherByAddress_returnsOk() throws Exception {
        WeatherResponse response = WeatherResponseMother.bucharest();

        when(weatherService.getWeatherByAddress("Bucharest", 5)).thenReturn(response);

        mockMvc.perform(get("/weather")
                .param("address", "Bucharest")
                .param("days", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location.name").value("Bucharest"));
    }

    @Test
    void getWeather_missingParams_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/weather"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void getWeather_withCoordinates_returnsOk() throws Exception {
        WeatherResponse response = WeatherResponseMother.unknownGeoLocation();
        
        when(weatherService.getWeatherByCoordinates(52.52, 13.41, 5, null)).thenReturn(response);

        mockMvc.perform(get("/weather")
                .param("lat", "52.52")
                .param("lon", "13.41")
                .param("days", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location.lat").value(52.52));
    }
}
