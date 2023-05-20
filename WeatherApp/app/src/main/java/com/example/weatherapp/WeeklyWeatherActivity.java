package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeeklyWeatherActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private static final String TAG = "SevenDayWeatherActivity";
    private String temperatureUnit = "us";
    private final List<WeeklyWeather> dailyWeatherItems = new ArrayList<>();
    private WeeklyWeatherAdapter weeklyWeatherAdapter;
    private LinearLayoutManager linearLayoutManager;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_weather);

        actionBarDefault();

        defaultLinearLayout();

        if (getIntent().hasExtra("TITLE")) {
            String name = getIntent().getStringExtra("TITLE");
            setTitle(name + " 15 Day");
        }

        if (getIntent().hasExtra("UNIT")) {
            temperatureUnit = getIntent().getStringExtra("UNIT");
        }

        if (getIntent().hasExtra("DATA")) {
            String data = getIntent().getStringExtra("DATA");
            try {
                JSONObject json =  new JSONObject(data);
                JSONArray days = json.getJSONArray("days");
                for(int i = 0; i<15 ; i++) {
                    JSONArray jHours = ((JSONObject) days.get(i)).getJSONArray("hours");
                        String tempUnit = temperatureScale();

                        String dateTime = getDataFormat(((JSONObject) days.get(i)).getLong("datetimeEpoch"));
                        String weekTemperature = ((JSONObject) days.get(i)).getString("tempmax") + tempUnit +"/" + ((JSONObject) days.get(i)).getString("tempmin") + tempUnit;
                        String weekImageView = ((JSONObject) days.get(i)).getString("icon");
                        String weekDescription = ((JSONObject) days.get(i)).getString("description");
                        String weekPrecipitation = "(" + ((JSONObject) days.get(i)).getString("precipprob") + "% precip.)";
                        String weekUvIndex = "UV Index: " + ((JSONObject) days.get(i)).getString("uvindex");
                        String weekMorningTemp = ((JSONObject) jHours.get(8)).getString("temp") + tempUnit;
                        String weekAfternoonTemp = ((JSONObject) jHours.get(13)).getString("temp") + tempUnit;
                        String weekEveningTemp = ((JSONObject) jHours.get(17)).getString("temp") + tempUnit;
                        String weekNightTemp = ((JSONObject) jHours.get(22)).getString("temp") + tempUnit;

                        WeeklyWeather newWeather = new WeeklyWeather(dateTime, identifierValue(weekImageView), weekTemperature, weekDescription, weekPrecipitation, weekUvIndex, weekMorningTemp, weekAfternoonTemp,  weekEveningTemp, weekNightTemp);
                    dailyWeatherItems.add(newWeather);
                }
                weeklyWeatherAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }


    private String temperatureScale(){
        return "°" + (temperatureUnit.equals("us") ? "F" : "C");
    }

    private void actionBarDefault(){
        // Set default title
        setTitle("Weather App");
        // Set ActionBar background color
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#61553f"));
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setBackgroundDrawable(cd);
        }
    }

    private void defaultLinearLayout(){
        recyclerView = findViewById(R.id.week_recycler);
        weeklyWeatherAdapter = new WeeklyWeatherAdapter(dailyWeatherItems, this);
        recyclerView.setAdapter(weeklyWeatherAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private Integer identifierValue(String string){
        String dash = string.replace("-", "_"); // Replace all dashes with underscores
        int image = WeeklyWeatherActivity.this.getResources().getIdentifier(dash, "drawable", WeeklyWeatherActivity.this.getPackageName());
        if (image == 0) {
            Log.d(TAG, "parseCurrentRecord: CANNOT FIND ICON " + dash);
        }
        return image;
    }

    private String getDataFormat(Long e){
        Date date = new Date(e * 1000);
        SimpleDateFormat dateForm = new SimpleDateFormat("EEEE, dd", Locale.getDefault());
        return dateForm.format(date);
    }

}