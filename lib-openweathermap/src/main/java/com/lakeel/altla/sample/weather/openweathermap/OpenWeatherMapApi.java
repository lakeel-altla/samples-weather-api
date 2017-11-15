package com.lakeel.altla.sample.weather.openweathermap;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.lakeel.altla.sample.weather.openweathermap.retrofit.CurrentWeather;
import com.lakeel.altla.sample.weather.openweathermap.retrofit.WeatherService;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherMapApi {

    private static final String BASE_URL_MAPS_API = "https://api.openweathermap.org/data/2.5/";

    private String key;

    private Gson gson;

    private WeatherService service;

    public OpenWeatherMapApi(@NonNull String key, @NonNull OkHttpClient httpClient) {
        this.key = key;

        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL_MAPS_API)
                                                  .client(httpClient)
                                                  .addConverterFactory(GsonConverterFactory.create(gson))
                                                  .build();

        this.service = retrofit.create(WeatherService.class);
    }

    @NonNull
    public Gson getGson() {
        return gson;
    }

    @NonNull
    public CurrentWeather getWeatherByCoodinates(double latitude, double longitude) {
        Call<CurrentWeather> call = service.getWeatherByCoodinates(key, latitude, longitude);

        try {
            Response<CurrentWeather> response = call.execute();
            CurrentWeather currentWeather = response.body();
            Objects.requireNonNull(currentWeather, "A response body is null.");
            return currentWeather;
        } catch (IOException e) {
            throw new OpenWeatherMapApiException(e);
        }
    }

    public class OpenWeatherMapApiException extends RuntimeException {

        public OpenWeatherMapApiException(Throwable cause) {
            super(cause);
        }
    }
}
