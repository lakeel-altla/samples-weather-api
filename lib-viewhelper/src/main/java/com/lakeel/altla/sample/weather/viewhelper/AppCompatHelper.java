package com.lakeel.altla.sample.weather.viewhelper;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;

import java.util.Objects;

public final class AppCompatHelper {

    private AppCompatHelper() {
    }

    public static void back(@NonNull Fragment fragment) {
        FragmentActivity activity = fragment.getActivity();
        Objects.requireNonNull(activity);
        back(activity);
    }

    public static void back(@NonNull FragmentActivity activity) {
        if (0 < activity.getSupportFragmentManager().getBackStackEntryCount()) {
            activity.getSupportFragmentManager().popBackStack();
        } else {
            NavUtils.navigateUpFromSameTask(activity);
        }
    }

    public static void setToolbarAsSupportActionBar(@NonNull AppCompatActivity activity, @IdRes int toolbarId) {
        Toolbar toolbar = activity.findViewById(toolbarId);
        Objects.requireNonNull(toolbar);
        activity.setSupportActionBar(toolbar);
    }

    @NonNull
    public static ActionBar getRequiredSupportActionBar(@NonNull Fragment fragment) {
        return getRequiredSupportActionBar(getAppCompatActivity(fragment));
    }

    @NonNull
    public static ActionBar getRequiredSupportActionBar(@NonNull AppCompatActivity activity) {
        return getRequiredSupportActionBar(activity.getDelegate());
    }

    @NonNull
    public static ActionBar getRequiredSupportActionBar(@NonNull AppCompatDelegate delegate) {
        ActionBar actionBar = delegate.getSupportActionBar();
        if (actionBar == null) {
            throw new IllegalStateException("No action bar is supported.");
        }
        return actionBar;
    }

    @NonNull
    private static AppCompatActivity getAppCompatActivity(@NonNull Fragment fragment) {
        FragmentActivity activity = fragment.getActivity();
        if (activity instanceof AppCompatActivity) {
            return (AppCompatActivity) activity;
        } else {
            throw new IllegalStateException("'fragment.activity' isn't an instance of the AppCompatActivity class.");
        }
    }

    public static void replaceFragment(@NonNull AppCompatActivity activity,
                                       @IdRes int containerViewId, @NonNull Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .replace(containerViewId, fragment, fragment.getClass().getName())
                .commit();
    }

    public static void replaceFragmentAndAddToBackStack(@NonNull AppCompatActivity activity,
                                                        @IdRes int containerViewId, @NonNull Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .addToBackStack(fragment.getClass().getName())
                .replace(containerViewId, fragment, fragment.getClass().getName())
                .commit();
    }
}

