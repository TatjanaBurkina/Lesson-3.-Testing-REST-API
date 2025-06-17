package com.example.api;

import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

public class AccuWeatherApiTests {

    private static final String API_KEY = "ft636sbc6xTE3spOQyLTX3k8dZucd6hw";
    private static final String BASE_URL = "http://dataservice.accuweather.com";
    private static final String LOCATION_KEY = "225780"; // Рига

    @Test
    void testGetCurrentWeather() {
        RestAssured.baseURI = BASE_URL;

        Response response = given()
            .queryParam("apikey", API_KEY)
        .when()
            .get("/currentconditions/v1/" + LOCATION_KEY)
        .then()
            .assertThat()
            .statusCode(200)
            .body("[0].Temperature.Metric.Value", notNullValue())
            .body("[0].WeatherText", notNullValue())
            .extract().response();

        System.out.println("Температура: " + response.jsonPath().getString("[0].Temperature.Metric.Value"));
        System.out.println("Погода: " + response.jsonPath().getString("[0].WeatherText"));
    }
}
