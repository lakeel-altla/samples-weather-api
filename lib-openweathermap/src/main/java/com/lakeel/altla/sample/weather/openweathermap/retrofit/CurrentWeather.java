package com.lakeel.altla.sample.weather.openweathermap.retrofit;

import com.lakeel.altla.sample.weather.openweathermap.Snow;
import com.lakeel.altla.sample.weather.openweathermap.Coord;
import com.lakeel.altla.sample.weather.openweathermap.Main;
import com.lakeel.altla.sample.weather.openweathermap.Rain;
import com.lakeel.altla.sample.weather.openweathermap.Sys;
import com.lakeel.altla.sample.weather.openweathermap.Weather;
import com.lakeel.altla.sample.weather.openweathermap.Wind;

import java.util.List;

public class CurrentWeather {

    public Coord coord;

    public List<Weather> weather;

    public String base;

    public Main main;

    public Wind wind;

    public Rain rain;

    public Snow snow;

    public long dt;

    public Sys sys;

    public long id;

    public String name;

    public long cod;
}
