package com.gubo.check_weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rain {
    @JsonProperty("3h")
    private double rainVolumeLastThreeHour;

    @JsonProperty("1h")
    private double rainVolumeLastHour;
}
