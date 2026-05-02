package com.canda.weatherapi;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

import static com.canda.weatherapi.utils.FileUtils.readFileAsString;
import static com.github.tomakehurst.wiremock.stubbing.StubMapping.buildFrom;
import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "weather.api.geoapify.base-url=${wiremock.server.baseUrl}/geo",
        "weather.api.open-meteo.base-url=${wiremock.server.baseUrl}/meteo"
    }
)
@EnableWireMock
class WeatherapiApplicationTests {

    @LocalServerPort
    private int port;

    @InjectWireMock
    private WireMockServer wireMock;

    @BeforeEach
    void setup() {

        RestAssured.port = port;
        wireMock.resetAll();
    }

    @Test
    void testWeatherByCoordinates() {

        wireMock.addStubMapping(buildFrom(readFileAsString("wiremock-stubs/mappings/open-meteo-coordinates.json")));

        given()
                .queryParam("lat", 52.52)
                .queryParam("lon", 13.41)
                .queryParam("days", 1)
                .when()
                .get("/weather")
                .then()
                .statusCode(200)
                .body(jsonEquals(readFileAsString("json/weather-by-coordinates-response.json")));
    }

    @Test
    void testWeatherByAddress() {

        wireMock.addStubMapping(buildFrom(readFileAsString("wiremock-stubs/mappings/geoapify-address.json")));
        wireMock.addStubMapping(buildFrom(readFileAsString("wiremock-stubs/mappings/open-meteo-address.json")));

        given()
                .queryParam("address", "Bucharest")
                .queryParam("days", 1)
                .when()
                .get("/weather")
                .then()
                .statusCode(200)
                .body(jsonEquals(readFileAsString("json/weather-by-address-response.json")));
    }

    @Test
    void testMissingParams_returnsBadRequest() {
        given()
                .when()
                .get("/weather")
                .then()
                .statusCode(400)
                .body(jsonEquals(readFileAsString("json/error-missing-params-response.json")));
    }

    @Test
    void testInvalidDays_returnsBadRequest() {
        given()
                .queryParam("lat", 52.52)
                .queryParam("lon", 13.41)
                .queryParam("days", 99)
                .when()
                .get("/weather")
                .then()
                .statusCode(400)
                .body(jsonEquals(readFileAsString("json/error-invalid-days-response.json")));
    }

    @Test
    void testUpstreamFailure_returnsBadGateway() {
        wireMock.addStubMapping(buildFrom(readFileAsString("wiremock-stubs/mappings/open-meteo-upstream-failure.json")));

        given()
                .queryParam("lat", 10.0)
                .queryParam("lon", 20.0)
                .queryParam("days", 1)
                .when()
                .get("/weather")
                .then()
                .statusCode(502)
                .body(jsonEquals(readFileAsString("json/error-upstream-failure-response.json")));
    }
}
