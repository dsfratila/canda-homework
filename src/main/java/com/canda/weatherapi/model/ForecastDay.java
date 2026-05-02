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
public class ForecastDay {
    private String date;
    
    @JsonProperty("min_temp")
    private Double minTemp;
    
    @JsonProperty("max_temp")
    private Double maxTemp;
    
    private String condition;
    
    @JsonProperty("precipitation_chance")
    private Double precipitationChance;
}
