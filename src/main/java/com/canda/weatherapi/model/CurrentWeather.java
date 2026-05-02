package com.canda.weatherapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentWeather {
    private Double temperature;
    private Double humidity;
    
    @JsonProperty("wind_speed")
    private Double windSpeed;
    
    private String condition;
    private String timestamp;
}
