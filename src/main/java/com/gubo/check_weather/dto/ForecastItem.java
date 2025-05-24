package com.gubo.check_weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ForecastItem {
    @JsonProperty("dt_txt")
    private String date;

    private String clothingRecommendation = "추천 데이터 없음";

    private Main main;  // 온도 관련 정보

    private Wind wind;  // 바람 정보

    private Rain rain;  // 강수량 정보

    private List<Weather> weather;  // 날씨 설명

    // 습도, 구름 양
}
