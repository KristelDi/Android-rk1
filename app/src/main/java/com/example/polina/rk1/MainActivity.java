package com.example.polina.rk1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.mail.weather.lib.City;
import ru.mail.weather.lib.Weather;
import ru.mail.weather.lib.WeatherStorage;
import ru.mail.weather.lib.WeatherUtils;

public class MainActivity extends AppCompatActivity {

    protected int refreshing;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            System.out.println(intent.getAction());
            String action = intent.getAction();

            TextView view1 = (TextView) findViewById(R.id.textView2);
            TextView view2 = (TextView) findViewById(R.id.textView);

            if (action.equals("WHEATHER_CHANGED_ACTION")) {
                Intent service_intent = new Intent(MainActivity.this, WetherService.class);
                startService(service_intent);
                City city = WeatherStorage.getInstance(MainActivity.this).getCurrentCity();
                Weather cur_weather = WeatherStorage.getInstance(MainActivity.this).getLastSavedWeather(city);
                view1.setText(String.valueOf(cur_weather.getTemperature()));
                view2.setText(cur_weather.getDescription());
            }
            if ( action.equals("WHEATHER_ERROR_ACTION")) {
                view1.setText("Something went wrong!");
            }
        }
    };

    private final View.OnClickListener onBroadcastClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Intent intent = new Intent(MainActivity.this, Main2Activity.class);
            startActivity(intent);
        }
    };

    private final View.OnClickListener onRefreshing = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, WetherService.class);
            if (refreshing == 0) {
                refreshing = 1;
                WeatherUtils.getInstance().schedule(MainActivity.this, intent);
            } else {
                refreshing = 0;
                WeatherUtils.getInstance().unschedule(MainActivity.this, intent);
            }
            setResfreshingButtom();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshing = 0;
        setResfreshingButtom();

        findViewById(R.id.button6).setOnClickListener(onBroadcastClick);
        findViewById(R.id.button7).setOnClickListener(onRefreshing);

        final IntentFilter filter = new IntentFilter();
        filter.addAction(WetherService.ACTION_CHANGED);
        filter.addAction(WetherService.ACTION_ERROR);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        System.out.print("REFRESHING \n");
//        System.out.print(this.refreshing);
//        System.out.print("\n");

        setResfreshingButtom();

        TextView view_main = (TextView) findViewById(R.id.textView4);
        TextView view1 = (TextView) findViewById(R.id.textView2);
        TextView view2 = (TextView) findViewById(R.id.textView);

        City city = WeatherStorage.getInstance(this).getCurrentCity();
        view_main.setText(city.toString());

        Weather cur_weather = WeatherStorage.getInstance(this).getLastSavedWeather(city);
        if (cur_weather != null) {
            view1.setText(String.valueOf(cur_weather.getTemperature()));
            view2.setText(cur_weather.getDescription());
        } else {
            view1.setText("smth1");
            view2.setText("smth2");
        }
    }

    void setResfreshingButtom() {
        Button btn_refreshing = (Button) findViewById(R.id.button7);
        if (refreshing == 0) {
            btn_refreshing.setText("Start refreshing");
        } else {
            btn_refreshing.setText("Stop refreshing");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}
