package com.lakeel.altla.sample.weather.openweathermap.di.module;

import com.lakeel.altla.sample.weather.openweathermap.BuildConfig;
import com.lakeel.altla.sample.weather.openweathermap.app.MyApplication;
import com.lakeel.altla.sample.weather.openweathermap.OpenWeatherMapApi;

import android.support.annotation.NonNull;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class ApplicationModule {

    private MyApplication application;

    public ApplicationModule(@NonNull MyApplication application) {
        this.application = application;
    }

    @Named(Names.OPENWEATHERMAP_API_KEY)
    @Singleton
    @Provides
    String provideGoogleApiKey() {
        return application.getString(com.lakeel.altla.weatherreport.res.R.string.openweathermap_api_key);
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        return builder.build();
    }

    @Singleton
    @Provides
    OpenWeatherMapApi provideOpenWeatherMapApi(@Named(Names.OPENWEATHERMAP_API_KEY) String key,
                                               OkHttpClient httpClient) {
        return new OpenWeatherMapApi(key, httpClient);
    }
}
