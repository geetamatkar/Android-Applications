package com.example.weatherapp;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CurrentWeatherView extends RecyclerView.ViewHolder {
    private static final String TAG = "DailyWeatherViewHolder";
    ImageView image;
    TextView day, dateTime;
    TextView temperature, constrains;
    CurrentWeatherView(View view){
        super(view);
        Log.d(TAG, "DailyWeatherViewHolder: ");

        day = view.findViewById(R.id.day);
        temperature = view.findViewById(R.id.temperature);
        image = view.findViewById(R.id.image);
        constrains = view.findViewById(R.id.constrains);
        dateTime = view.findViewById(R.id.dateTime);
    }
}