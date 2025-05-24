package com.gubo.check_weather.service;

import org.springframework.stereotype.Service;

@Service
public class ClothingRecommendationService {
    public String recommendClothing(double feelsLikeTemp) {
        if (feelsLikeTemp <= 5) {
            return "두꺼운 외투(패딩, 두꺼운 코트), 목도리, 장갑, 기모소재";
        } else if (feelsLikeTemp <= 10) {
            return "외투(울 코트, 가죽 재킷), 히트텍, 기모소재";
        } else if (feelsLikeTemp <= 15) {
            return "가벼운 외투(재킷, 가디건), 니트, 청바지";
        } else if (feelsLikeTemp <= 22) {
            return "얇은 니트, 후드티, 셔츠, 블라우스, 가디건";
        } else if (feelsLikeTemp <= 27) {
            return "반소매, 얇은 셔츠, 반바지, 면바지";
        } else {
            return "민소매, 반소매, 린넨 소재의 시원한 옷";
        }
    }
}
