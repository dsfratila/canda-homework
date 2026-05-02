package com.canda.weatherapi.client;

import com.canda.weatherapi.client.openmeteo.OpenMeteoResponse;
import com.canda.weatherapi.config.RestClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(OpenMeteoClient.class)
@Import(RestClientConfig.class)
public class OpenMeteoClientTest {

    @Autowired
    private OpenMeteoClient openMeteoClient;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void fetchWeather_returnsOpenMeteoResponse() {
        String jsonPayload = """
            {
              "latitude": 44.43,
              "longitude": 26.1,
              "current": {
                "time": "2026-05-01T16:00",
                "temperature_2m": 22.5
              }
            }
        """;

        server.expect(requestTo("https://api.open-meteo.com/v1/forecast?latitude=44.43&longitude=26.1&current=temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max&forecast_days=5&timezone=UTC"))
                .andRespond(withSuccess(jsonPayload, MediaType.APPLICATION_JSON));

        OpenMeteoResponse response = openMeteoClient.fetchWeather(44.43, 26.1, 5);

        assertThat(response.getLatitude()).isEqualTo(44.43);
        assertThat(response.getCurrent().getTemperature2m()).isEqualTo(22.5);
    }
}
