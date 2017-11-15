package com.lakeel.altla.sample.weather.openweathermap.di.component;

import com.lakeel.altla.sample.weather.openweathermap.di.module.ApplicationModule;
import com.lakeel.altla.sample.weather.openweathermap.di.module.ActivityModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { ApplicationModule.class })
public interface ApplicationComponent {

    ActivityComponent activityComponent(ActivityModule module);
}
