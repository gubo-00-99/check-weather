package com.gubo.check_weather.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {
    private String cityName;
    double lat;
    double lon;
}
