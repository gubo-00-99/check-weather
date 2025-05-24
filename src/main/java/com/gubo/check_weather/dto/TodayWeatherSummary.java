package com.gubo.check_weather.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TodayWeatherSummary {
    private List<ForecastItem> todayForecastItems;
    private City city;
    // 대표 요약용 정보
    private String date;
    private String clothingRecommendation;
    private String weatherMain; // 예: "Clear", "Rain"
    private double tempMin;
    private double tempMax;
    private double tempAvg;
    private double feelsTempAvg;
    private double windSpeedAvg;
    private double totalRainVolume;
    private boolean isRaining;
}
