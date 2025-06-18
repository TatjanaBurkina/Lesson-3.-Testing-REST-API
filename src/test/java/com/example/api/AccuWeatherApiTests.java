// package com.example.api;

// import static org.hamcrest.Matchers.notNullValue;
// import org.junit.jupiter.api.Test;

// import io.restassured.RestAssured;
// import static io.restassured.RestAssured.given;
// import io.restassured.response.Response;

// public class AccuWeatherApiTests {

//     private static final String API_KEY = "ft636sbc6xTE3spOQyLTX3k8dZucd6hw";
//     private static final String BASE_URL = "http://dataservice.accuweather.com";
//     private static final String LOCATION_KEY = "225780"; // Рига

//     @Test
//     void testGetCurrentWeather() {
//         RestAssured.baseURI = BASE_URL;

//         Response response = given()
//             .queryParam("apikey", API_KEY)
//         .when()
//             .get("/currentconditions/v1/" + LOCATION_KEY)
//         .then()
//             .assertThat()
//             .statusCode(200)
//             .body("[0].Temperature.Metric.Value", notNullValue())
//             .body("[0].WeatherText", notNullValue())
//             .extract().response();

//         System.out.println("Температура: " + response.jsonPath().getString("[0].Temperature.Metric.Value"));
//         System.out.println("Погода: " + response.jsonPath().getString("[0].WeatherText"));
//     }
// }

package com.example.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class AccuWeatherApiTests {

    private static final String API_KEY = "ft636sbc6xTE3spOQyLTX3k8dZucd6hw";
    private static final String BASE_URL = "http://dataservice.accuweather.com";
    private static final String LOCATION = "225780"; // Riga

    @Test void searchCityByName() {
        given().queryParam("q", "Riga").queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/locations/v1/cities/search")
        .then().statusCode(200).body("[0].Key", equalTo(LOCATION));
    }

    @Test void getBasicCurrentConditions() {
        given().queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/currentconditions/v1/" + LOCATION)
        .then().statusCode(200).body("[0].WeatherText", notNullValue());
    }

    @Test void getDetailedCurrentConditions() {
        given().queryParam("details", true).queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/currentconditions/v1/" + LOCATION)
        .then().statusCode(200).body("[0].Pressure.Metric.Value", notNullValue());
    }

    @Test void getHistoricalConditions24h() {
        given().queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/currentconditions/v1/" + LOCATION + "/historical/24")
        .then().statusCode(200).body("size()", greaterThan(1));
    }

    @Test void get5DayForecast() {
        given().queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/forecasts/v1/daily/5day/" + LOCATION)
        .then().statusCode(200).body("DailyForecasts.size()", equalTo(5));
    }

    @Test void getHourlyForecast12h() {
        given().queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/forecasts/v1/hourly/12hour/" + LOCATION)
        .then().statusCode(200).body("size()", equalTo(12));
    }

    @Test void getHourlyForecast1h() {
        given().queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/forecasts/v1/hourly/1hour/" + LOCATION)
        .then().statusCode(200).body("size()", equalTo(1));
    }

    @Test void getLocationDetails() {
        given().queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/locations/v1/" + LOCATION)
        .then().statusCode(200).body("LocalizedName", equalTo("Riga"));
    }

    @Test void getNeighboringCities() {
        given().queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/locations/v1/cities/neighbors/" + LOCATION)
        .then().statusCode(200).body("size()", greaterThan(0));
    }

    @Test void getRelativeHumidity() {
        given().queryParam("details", true).queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/currentconditions/v1/" + LOCATION)
        .then().statusCode(200).body("[0].RelativeHumidity", notNullValue());
    }

    @Test void getUVIndexFromDetails() {
        given().queryParam("details", true).queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/currentconditions/v1/" + LOCATION)
        .then().statusCode(200).body("[0].UVIndex", notNullValue());
    }

    @Test void getCloudCoverFromDetails() {
        given().queryParam("details", true).queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/currentconditions/v1/" + LOCATION)
        .then().statusCode(200).body("[0].CloudCover", notNullValue());
    }

    @Test void getIsDayTimeFlag() {
        given().queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/currentconditions/v1/" + LOCATION)
        .then().statusCode(200).body("[0].IsDayTime", notNullValue());
    }

    @Test void getWeatherIconId() {
        given().queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/currentconditions/v1/" + LOCATION)
        .then().statusCode(200).body("[0].WeatherIcon", notNullValue());
    }

    @Test void getForecastHeadline() {
        given().queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/forecasts/v1/daily/5day/" + LOCATION)
        .then().statusCode(200).body("Headline.Text", notNullValue());
    }

    @Test void verifyMinTemperature() {
        given().queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/forecasts/v1/daily/5day/" + LOCATION)
        .then().statusCode(200).body("DailyForecasts[0].Temperature.Minimum.Value", notNullValue());
    }

    @Test void verifyMaxTemperature() {
        given().queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/forecasts/v1/daily/5day/" + LOCATION)
        .then().statusCode(200).body("DailyForecasts[0].Temperature.Maximum.Value", notNullValue());
    }

    @Test void checkRainProbability() {
        given().queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/forecasts/v1/daily/5day/" + LOCATION)
        .then().statusCode(200).body("DailyForecasts[0].Day", hasKey("RainProbability"));
    }

    @Test void checkSnowProbability() {
        given().queryParam("apikey", API_KEY)
        .when().get(BASE_URL + "/forecasts/v1/daily/5day/" + LOCATION)
        .then().statusCode(200).body("DailyForecasts[0].Day", hasKey("SnowProbability"));
    }
}
