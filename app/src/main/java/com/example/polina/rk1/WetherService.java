package com.example.polina.rk1;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import java.io.IOException;

import ru.mail.weather.lib.City;
import ru.mail.weather.lib.Weather;
import ru.mail.weather.lib.WeatherStorage;
import ru.mail.weather.lib.WeatherUtils;

public class WetherService extends IntentService {

    public final static String ACTION_ERROR = "WHEATHER_ERROR_ACTION";
    public final static String ACTION_CHANGED = "WHEATHER_CHANGED_ACTION";

    public WetherService() {
        super("WetherService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        City city = WeatherStorage.getInstance(this).getCurrentCity();
        Weather wether;
        WeatherUtils ut = WeatherUtils.getInstance();
        try {
            wether = ut.loadWeather(city);
            WeatherStorage.getInstance(this).saveWeather(city, wether);
            // рассылка бродкаст
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ACTION_CHANGED));
        } catch (IOException e) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ACTION_ERROR));
            e.printStackTrace();
        }
    }
}
