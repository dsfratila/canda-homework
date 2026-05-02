package com.canda.weatherapi.client;

import com.canda.weatherapi.client.geoapify.GeoapifyResponse;
import com.canda.weatherapi.model.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GeoapifyClient {

    private final RestClient restClient;
    private final String baseUrl;
    private final String apiKey;

    public GeoapifyClient(RestClient restClient,
                          @Value("${weather.api.geoapify.base-url}") String baseUrl,
                          @Value("${weather.api.geoapify.api-key}") String apiKey) {
        this.restClient = restClient;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    @Cacheable(value = "geoCache", key = "#address")
    public Location resolveAddress(String address) {
        String uri = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/geocode/search")
                .queryParam("text", address)
                .queryParam("format", "json")
                .queryParam("apiKey", apiKey)
                .build().toUriString();

        GeoapifyResponse response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(GeoapifyResponse.class);

        if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
            throw new IllegalArgumentException("Address could not be resolved or is invalid.");
        }

        GeoapifyResponse.Result firstResult = response.getResults().getFirst();
        return Location.builder()
                .name(firstResult.getFormatted())
                .lat(firstResult.getLat())
                .lon(firstResult.getLon())
                .build();
    }
}
