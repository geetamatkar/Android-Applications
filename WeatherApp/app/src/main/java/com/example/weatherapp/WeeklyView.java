package com.example.weatherapp;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class WeeklyView extends RecyclerView.ViewHolder {
    private static final String TAG = "SevenDayViewHolder";
     TextView weekTemperature, weekDateTimeEpoch, weekDescription;
     TextView weekPrecipitation, weekUvIndex;
     TextView weekMorningTemp, weekAfternoonTemp, weekEveningTemp, weekNightTemp;
     ImageView weekImageView;

    WeeklyView(View view){
        super(view);
        Log.d(TAG, "SevenDayViewHolder: ");

        weekDateTimeEpoch = view.findViewById(R.id.weekDateTimeEpoch);
        weekTemperature = view.findViewById(R.id.weekTemperature);
        weekUvIndex = view.findViewById(R.id.weekUvIndex);
        weekPrecipitation = view.findViewById(R.id.weekPrecipitation);
        weekEveningTemp = view.findViewById(R.id.weekEveningTemp);
        weekMorningTemp = view.findViewById(R.id.weekMorningTemp);
        weekAfternoonTemp = view.findViewById(R.id.weekAfternoonTemp);
        weekImageView = view.findViewById(R.id.weekImageView);
        weekNightTemp = view.findViewById(R.id.weekNightTemp);
        weekDescription = view.findViewById(R.id.weekDescription);

    }
}
