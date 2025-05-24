document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("#weatherForm");
    const cityInput = document.querySelector("#cityInput");
    const weatherInfoCard = document.querySelector("#weatherInfo");
    const todayDate = document.querySelector("#todayDate");
    const locationName = document.querySelector("#locationName");
    const temperature = document.querySelector("#temperature");
    const lowestTemp = document.querySelector("#lowestTemp");
    const highestTemp = document.querySelector("#highestTemp");
    const perceivedTemp = document.querySelector("#perceivedTemp");
    const windSpeed = document.querySelector("#windSpeed");
    const precipitation = document.querySelector("#precipitation");
    const weatherDescription = document.querySelector("#weatherDescription");
    const clothingRecommendation = document.querySelector("#clothingRecommendation");
    const errorMessage = document.querySelector("#errorMessage");
    const weatherImg = document.querySelector("#weatherImg");
    const loadingSpinner = document.querySelector("#loadingSpinner");
    let selectedLat = null;
    let selectedLon = null;
    let selectedCity = null;

    // 도시명 자동완성 기능
    const autocomplete = new google.maps.places.Autocomplete(cityInput, {
        types: ["(cities)"],  // 도시만 제안
        componentRestrictions: { country: "kr" }  // 선택사항: 한국 내 제한
    })
    autocomplete.addListener("place_changed", () => {
        const place = autocomplete.getPlace();
        cityInput.value = place.name || place.formatted_address;
        selectedLat = place.geometry.location.lat();
        selectedLon = place.geometry.location.lng();
        selectedCity = place.name;
    })

    form.addEventListener("submit", async (e) => {  // submit 버튼이 눌렸을 때
        e.preventDefault();  // 폼 제출 막기
        errorMessage.textContent = "";
        const city = cityInput.value.trim();

        // 도시명이 비어있을 때 대비
        if (!city) {
            errorMessage.textContent = "도시명을 입력해주세요.";
            return;
        }
        loadingSpinner.style.display = 'block';
        try {
            // const weatherResponse = await fetch("/weather?city=" + encodeURIComponent(city)); // 도시명을 백엔드로 보내서 weatherAPI에서 데이터 받아오기
            const weatherResponse = await fetch("/weather/forecast?lat=" + selectedLat + "&lon=" + selectedLon + "&cityName=" + encodeURIComponent(selectedCity));
            const data = await weatherResponse.json();
            loadingSpinner.style.display = 'none';
            // 받아온 데이터 화면 출력
            // locationName.textContent = "도시: " + data.city.cityName;
            todayDate.textContent = "오늘의 날짜: " + data.date;
            temperature.textContent = "평균 기온: " + data.tempAvg.toFixed(1) + "°C";
            lowestTemp.textContent = "최저 기온: " + data.tempMin.toFixed(1) + "°C";
            highestTemp.textContent = "최고 기온: " + data.tempMax.toFixed(1) + "°C";
            perceivedTemp.textContent = "체감 온도 평균: " + data.feelsTempAvg.toFixed(1) + "°C";
            windSpeed.textContent = "평균 풍속: " + data.windSpeedAvg.toFixed(1) + "m/s";
            if (data.isRaining) {
                precipitation.textContent = "총 강수량: " + data.totalRainVolume.toFixed(1) + "mm";
            } else {
                precipitation.textContent = ""; // 또는 숨기기
            }
            weatherDescription.textContent = "날씨 상태: " + data.weatherMain;
            clothingRecommendation.textContent = "추천 옷차림: " + data.clothingRecommendation;

            // 오늘의 날짜 출력
            let today = new Date();
            const week = ["일", "월", "화", "수", "목", "금", "토"];
            todayDate.textContent =
                today.getFullYear() + "년 " +
                (today.getMonth() + 1) + "월 " +
                today.getDate() + "일 " +
                "(" + week[today.getDay()] + ")";

            // card 표시
            weatherInfoCard.style.display = "block";
            weatherInfoCard.scrollIntoView({ behavior: 'smooth' });
        } catch (error) {
            console.error("에러 발생:", error);
            loadingSpinner.style.display = 'none';
            errorMessage.textContent = "날씨 정보를 불러오는 데 실패했습니다.";
        }


    })

    // API에서 자동완성 기능을 제공해서 폐기
    //    // 도시명 자동완성 기능
    //    inputCity.addEventListener("keyup", function () {
    //        if (inputCity.value.length < 1) {
    //            list.innerHTML = "/weather";  // 입력이 없으면 자동완성 목록 초기화
    //            return;
    //        }
    //
    //        // 목록 초기화 -> li로 자동완성된 도시들 추가 -> 선택한 도시를 입력 필드에 넣기 -> 목록 초기화
    //        fetch("/api/places/autocomplete?input=" + encodeURIComponent(inputCity.value))
    //            .then(response => response.json())
    //            .then(data => {
    //                list.innerHTML = "";  // 목록 초기화
    //
    //                data.predictions.forEach(place => {  // 자동완성된 도시명 항목 하나마다
    //                    const listItem = document.createElement("li");  // li 요소를 추가하여
    //                    listItem.textContent = place.description;  // 도시명을 주입하고
    //                    listItem.onclick = () => {  // 요소가 클릭되면
    //                        inputCity.value = place.description;  // 선택한 도시를 입력 필드에 넣기
    //                        list.innerHTML = "";  // 목록 초기화
    //                    };
    //                    list.appendChild(listItem);  // 리스트를 화면에 보이게 만들기
    //
    //                });
    //            }).catch(console => console.error("Error fetching autocomplete: ", error));  // 에러나면 콘솔에 띄우기
    //    });

});
