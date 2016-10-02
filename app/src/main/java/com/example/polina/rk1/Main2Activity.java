package com.example.polina.rk1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ru.mail.weather.lib.City;
import ru.mail.weather.lib.WeatherStorage;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ViewGroup root = (ViewGroup) findViewById(R.id.root);

        for (final City city : City.values()) {
            Button button = new Button(this);
            button.setText(city.name());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WeatherStorage.getInstance(Main2Activity.this).setCurrentCity(city);

                    Intent service_intent = new Intent(Main2Activity.this, WetherService.class);
                    startService(service_intent);
                    finish();
                }
            });
            root.addView(button);
        }
    }
}


