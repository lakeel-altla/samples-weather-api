package com.lakeel.altla.sample.weather.openweathermap.app;

import com.lakeel.altla.android.log.Log;
import com.lakeel.altla.android.log.LogFactory;
import com.lakeel.altla.sample.weather.openweathermap.BuildConfig;
import com.lakeel.altla.sample.weather.openweathermap.di.component.ApplicationComponent;
import com.lakeel.altla.sample.weather.openweathermap.di.component.DaggerApplicationComponent;
import com.lakeel.altla.sample.weather.openweathermap.di.module.ApplicationModule;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;

import io.reactivex.plugins.RxJavaPlugins;

public final class MyApplication extends Application {

    private static final Log LOG = LogFactory.getLog(MyApplication.class);

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        LogFactory.setDebug(BuildConfig.DEBUG);

        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        // see: https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#error-handling
        RxJavaPlugins.setErrorHandler(e -> {
            LOG.e("An unhandled error.", e);
        });
    }

    public static ApplicationComponent getApplicationComponent(@NonNull Activity activity) {
        return ((MyApplication) activity.getApplication()).applicationComponent;
    }
}
