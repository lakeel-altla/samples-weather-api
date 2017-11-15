package com.lakeel.altla.sample.weather.openweathermap.retrofit;

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("weather")
    Call<CurrentWeather> getWeatherByCoodinates(@NonNull @Query("APPID") String appId,
                                                @Query("lat") double lat,
                                                @Query("lon") double lon);
}
