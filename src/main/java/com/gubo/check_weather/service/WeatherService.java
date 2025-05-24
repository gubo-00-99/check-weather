package com.gubo.check_weather.service;

import com.gubo.check_weather.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Value("${weather.api.key}")
    private String weatherApiKey;

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${forecast.api.url}")
    private String forecastApiUrl;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    // 위도와 경도로 OpenWeatherAPI에서 날씨 정보 받아오기
    public TodayWeatherSummary getTodayWeatherSummary(double lat, double lon, String cityName) {
        TodayWeatherSummary summary = new TodayWeatherSummary();
        ClothingRecommendationService recommendation = new ClothingRecommendationService();
        City city=new City();

        // 도시 관련 정보 summary에 저장
        city.setLat(lat);
        city.setLon(lon);
        city.setCityName(cityName);
        summary.setCity(city);

        // 위도와 경도에 맞는 주소의 5일치 날씨 정보 (ForecastResponse) 가져오기
        String url = "%s?lat=%s&lon=%s&appid=%s&units=metric&lang=kr"
                .formatted(forecastApiUrl, lat, lon, weatherApiKey);
        logger.info("Fetching forecast data from URL: {}", url);
        ResponseEntity<ForecastResponse> response = restTemplate.getForEntity(url, ForecastResponse.class);

        // 오늘 날짜 기준으로 예보 항목 필터링
        List<ForecastItem> forecastItems = response.getBody().getForecastItems();
        String todayDate = LocalDate.now().toString(); //20XX-XX-XX
        List<ForecastItem> todayItems = forecastItems.stream()
                .filter(forecastItem -> forecastItem.getDate().startsWith(todayDate)).toList();
        summary.setTodayForecastItems(todayItems); //akwsk?

        // 필터링된 List<ForecastItem>에서 하루 중 최저기온, 최고기온, 평균 기온, 평균 체감 기온, 풍속 평균, 강수량, 날씨 정보, 옷차림 추천 멘트
        boolean isRaining = false;
        double tempAvg = 0;
        double tempSum = 0;
        double feelsTempAvg = 0;
        double feelsTempSum = 0;
        double windSpeedAvg = 0;
        double windSpeedSum = 0;
        double tempMin = Double.MAX_VALUE;
        double tempMax = Double.MIN_VALUE;
        double totalRainVolume = 0;
        double minDiff = Double.MAX_VALUE;
        String weatherMain = ""; // 예: "Clear", "Rain"
        String clothingRecommendation = "";

        // 최저 기온 tempMin
        for (ForecastItem item : todayItems) {
            if (item.getMain().getTempMin() < tempMin) {
                tempMin = item.getMain().getTempMin();
            }
        }
        summary.setTempMin(tempMin);

        // 최고 기온 tempMax
        for (ForecastItem item : todayItems) {
            if (item.getMain().getTempMax() > tempMax) {
                tempMax = item.getMain().getTempMax();
            }
        }
        summary.setTempMax(tempMax);


        // 평균 기온 tempAvg
        for (ForecastItem item : todayItems) {
            tempSum += item.getMain().getTemp();
        }
        tempAvg = tempSum / todayItems.size();
        summary.setTempAvg(tempAvg);

        // 평균 체감 기온 feelsTempAvg
        for (ForecastItem item : todayItems) {
            feelsTempSum += item.getMain().getFeelsLikeTemp();
        }
        feelsTempAvg = feelsTempSum / todayItems.size();
        summary.setFeelsTempAvg(feelsTempAvg);

        // 평균 풍속 평균 windSpeedAvg
        for (ForecastItem item : todayItems) {
            windSpeedSum += item.getWind().getWindSpeed();
        }
        windSpeedAvg = windSpeedSum / todayItems.size();
        summary.setWindSpeedAvg(windSpeedAvg);

        // 강수량 정보 totalRainVolume
        for (ForecastItem item : todayItems) {
            if (item.getRain() != null) {
                totalRainVolume += item.getRain().getRainVolumeLastThreeHour();
                isRaining = true;
            }
        }
        summary.setTotalRainVolume(totalRainVolume);
        summary.setRaining(isRaining);


        // 날씨 설명 weatherMain
        for (ForecastItem item : todayItems) {
            if (Math.abs(item.getMain().getTemp() - tempAvg) < minDiff) {
                minDiff = Math.abs(item.getMain().getTemp() - tempAvg);
                weatherMain = item.getWeather().get(0).getWeatherDescription();
            }
        }
        summary.setWeatherMain(weatherMain);

        // 옷차림 추천 멘트
        clothingRecommendation = recommendation.recommendClothing(feelsTempAvg);
        summary.setClothingRecommendation(clothingRecommendation);

        return summary;

//        try {
//            return null;
//        } catch (Exception e) {
//            logger.error("Error fetching forecast data : lat={}, lon={}, msg={}", lat, lon, e.getMessage());
//            throw new RuntimeException("Failed to fetch forecast data for lat: " + lat + "lon: " + lon);
//        }
    }


    // 도시명으로 OpenWeatherAPI에서 날씨 정보 받아오기 -> 현재 사용 x 위도와 경도로 날씨 검색하도록 변경됨
    public WeatherResponse getWeatherByCity(String city) {
        String url = String.format("%s?q=%s&appid=%s&units=metric&lang=kr",
                weatherApiUrl,
                city,
                weatherApiKey);
        logger.info("Fetching weather data from URL: {}", url);

        try {
            ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(url, WeatherResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.warn("City not found: {}", city);
            throw new IllegalArgumentException("해당 도시의 날씨 정보를 찾을 수 없습니다.");
        } catch (Exception e) {
            logger.error("Error fetching weather data for city: {} - {}", city, e.getMessage());
            throw new RuntimeException("Failed to fetch weather data for city: " + city);
        }
    }
}
