package com.canda.weatherapi.client;

import com.canda.weatherapi.config.RestClientConfig;
import com.canda.weatherapi.model.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(GeoapifyClient.class)
@Import(RestClientConfig.class)
public class GeoapifyClientTest {

    @Autowired
    private GeoapifyClient geoapifyClient;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void resolveAddress_returnsLocation() {
        String jsonPayload = """
            {
              "results": [
                {
                  "lat": 44.43,
                  "lon": 26.1,
                  "city": "Bucharest",
                  "formatted": "Bucharest, Romania"
                }
              ]
            }
        """;

        server.expect(requestTo("https://api.geoapify.com/v1/geocode/search?text=Bucharest&format=json&apiKey=b7dd0f223148492eb0c0d6ecc5c859ee"))
                .andRespond(withSuccess(jsonPayload, MediaType.APPLICATION_JSON));

        Location loc = geoapifyClient.resolveAddress("Bucharest");

        assertThat(loc.getName()).isEqualTo("Bucharest, Romania");
        assertThat(loc.getLat()).isEqualTo(44.43);
    }
}
