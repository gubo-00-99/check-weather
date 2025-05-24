package com.gubo.check_weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Wind {
    @JsonProperty("speed")
    private double windSpeed;
}
