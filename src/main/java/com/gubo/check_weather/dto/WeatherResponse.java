package com.gubo.check_weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
//안씀 forecast를 호출하는 것으로 설계가 변경됨
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)  // 불필요한 JSON 필드 무시
public class WeatherResponse {

    private String clothingRecommendation = "추천 데이터 없음";

    private String name;  // 도시 이름

    private Main main;  // 온도 관련 정보

    private Wind wind;  // 바람 정보

    @JsonProperty("rain")
    private Rain rain;  // 강수량 정보

    private List<Weather> weather;  // 날씨 설명
}
