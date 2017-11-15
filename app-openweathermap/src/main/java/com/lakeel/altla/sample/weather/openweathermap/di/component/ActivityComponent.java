package com.lakeel.altla.sample.weather.openweathermap.di.component;

import com.lakeel.altla.sample.weather.openweathermap.view.activity.MainActivity;
import com.lakeel.altla.sample.weather.openweathermap.di.ActivityScope;
import com.lakeel.altla.sample.weather.openweathermap.di.module.ActivityModule;
import com.lakeel.altla.sample.weather.openweathermap.view.fragment.WeatherFragment;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = { ActivityModule.class })
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(WeatherFragment fragment);
}
