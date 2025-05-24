package com.gubo.check_weather;

import com.gubo.check_weather.dto.ForecastResponse;
import com.gubo.check_weather.dto.TodayWeatherSummary;
import com.gubo.check_weather.dto.WeatherResponse;
import com.gubo.check_weather.service.ClothingRecommendationService;
import com.gubo.check_weather.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@Controller
@RestController
@RequestMapping("/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @GetMapping("/forecast")
    public ResponseEntity<?> getForecast(@RequestParam double lat, @RequestParam double lon, @RequestParam String cityName) {
        logger.info("Fetching forecast data for city: cityName={}, lat={}, lon={}",cityName, lat, lon);
        try {
            TodayWeatherSummary weather = weatherService.getTodayWeatherSummary(lat, lon, cityName);
            return ResponseEntity.ok(weather);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류로 인해 날씨 정보를 가져올 수 없습니다.");
        }
    }
}
