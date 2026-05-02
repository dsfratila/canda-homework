package com.canda.weatherapi.client.openmeteo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class OpenMeteoResponse {
    private Double latitude;
    private Double longitude;
    private Current current;
    private Daily daily;

    @Data
    public static class Current {
        private String time;
        @JsonProperty("temperature_2m")
        private Double temperature2m;
        @JsonProperty("relative_humidity_2m")
        private Double relativeHumidity2m;
        @JsonProperty("wind_speed_10m")
        private Double windSpeed10m;
        @JsonProperty("weather_code")
        private Integer weatherCode;
    }

    @Data
    public static class Daily {
        private List<String> time;
        @JsonProperty("weather_code")
        private List<Integer> weatherCode;
        @JsonProperty("temperature_2m_max")
        private List<Double> temperature2mMax;
        @JsonProperty("temperature_2m_min")
        private List<Double> temperature2mMin;
        @JsonProperty("precipitation_probability_max")
        private List<Double> precipitationProbabilityMax;
    }
}
