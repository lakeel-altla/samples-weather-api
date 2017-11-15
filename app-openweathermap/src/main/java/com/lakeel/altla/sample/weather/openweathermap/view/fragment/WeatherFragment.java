package com.lakeel.altla.sample.weather.openweathermap.view.fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.lakeel.altla.android.log.Log;
import com.lakeel.altla.android.log.LogFactory;
import com.lakeel.altla.sample.weather.openweathermap.R;
import com.lakeel.altla.sample.weather.openweathermap.di.ActivityScopeContext;
import com.lakeel.altla.sample.weather.locationpicker.LocationPickerActivity;
import com.lakeel.altla.sample.weather.openweathermap.OpenWeatherMapApi;
import com.lakeel.altla.sample.weather.openweathermap.retrofit.CurrentWeather;
import com.lakeel.altla.sample.weather.rxhelper.RxHelper;
import com.lakeel.altla.sample.weather.viewhelper.AppCompatHelper;
import com.lakeel.altla.sample.weather.viewhelper.FragmentHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.lakeel.altla.sample.weather.viewhelper.FragmentHelper.findViewById;

public class WeatherFragment extends Fragment {

    private static final Log LOG = LogFactory.getLog(WeatherFragment.class);

    private static final int REQUEST_CODE_LOCATION_PICKER = 100;

    @Inject
    OpenWeatherMapApi openWeatherMapApi;

    private FragmentContext fragmentContext;

    private ProgressBar progressBar;

    private TextView textViewJson;

    @Nullable
    private LatLng location;

    private boolean quering;

    @NonNull
    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentContext = (FragmentContext) context;
        ((ActivityScopeContext) context).getActivityComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentContext = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressBar = findViewById(this, R.id.progress_bar);
        progressBar.setVisibility(GONE);

        textViewJson = findViewById(this, R.id.text_view_json);

        FloatingActionButton floatingActionButton = findViewById(this, R.id.fab);
        floatingActionButton.setOnClickListener(v -> {
            fragmentContext.getLastLocation(lastLocation -> {
                if (lastLocation == null) {
                    LOG.w("The last location could not be resolved.");

                    location = null;

                    LOG.w("Trying to check location settings.");
                    fragmentContext.checkLocationSettings();
                } else {
                    location = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    getWeather();
                }
            }, e -> {
                LOG.e("Failed to get last location.", e);
            });
        });
        floatingActionButton.setOnLongClickListener(v -> {
            LocationPickerActivity.Builder builder =
                    new LocationPickerActivity.Builder(FragmentHelper.getRequiredContext(this))
                            .setMyLocationEnabled(true)
                            .setBuildingsEnabled(false)
                            .setIndoorEnabled(true)
                            .setTrafficEnabled(false)
                            .setMapToolbarEnabled(false)
                            .setZoomControlsEnabled(true)
                            .setMyLocationButtonEnabled(true)
                            .setCompassEnabled(true)
                            .setIndoorLevelPickerEnabled(true)
                            .setAllGesturesEnabled(true);

            if (location != null) {
                builder.setLocation(location.latitude, location.longitude);
            }

            Intent intent = builder.build();
            startActivityForResult(intent, REQUEST_CODE_LOCATION_PICKER);

            return true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FragmentHelper.getRequiredActivity(this).setTitle(R.string.title_weather);
        AppCompatHelper.getRequiredSupportActionBar(this).setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);

        quering = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentContext.startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentContext.stopLocationUpdates();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_LOCATION_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                location = LocationPickerActivity.getLocation(data);
                getWeather();
            } else {
                LOG.d("Picking a location is cancelled.");
            }
        }
    }

    private void getWeather() {
        if (location == null) return;
        if (quering) return;

        quering = true;

        double latitude = location.latitude;
        double longitude = location.longitude;

        progressBar.setVisibility(VISIBLE);

        RxHelper.disposeOnStop(this, Single
                .<CurrentWeather>create(e -> {
                    CurrentWeather currentWeather = openWeatherMapApi.getWeatherByCoodinates(latitude, longitude);
                    e.onSuccess(currentWeather);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currentWeather -> {
                    textViewJson.setText(openWeatherMapApi.getGson().toJson(currentWeather));

                    progressBar.setVisibility(GONE);
                    quering = false;
                }, e -> {
                    LOG.e("Failed to get current weather.", e);

                    progressBar.setVisibility(GONE);
                    quering = false;
                })
        );
    }

    public interface FragmentContext {

        void checkLocationSettings();

        void startLocationUpdates();

        void stopLocationUpdates();

        void getLastLocation(@NonNull OnSuccessListener<Location> onSuccessListener,
                             @NonNull OnFailureListener onFailureListener);
    }
}
