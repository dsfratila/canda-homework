package com.canda.weatherapi.client.geoapify;

import lombok.Data;
import java.util.List;

@Data
public class GeoapifyResponse {
    private List<Result> results;

    @Data
    public static class Result {
        private Double lat;
        private Double lon;
        private String city;
        private String formatted;
    }
}
