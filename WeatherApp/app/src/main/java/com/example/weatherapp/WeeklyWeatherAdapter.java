package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeeklyWeatherAdapter extends RecyclerView.Adapter<WeeklyView> {


    private final List<WeeklyWeather> weeklyWeatherItems;
    private static final String TAG = "WeekWeatherAdapter";

    @NonNull
    @Override
    public WeeklyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.week_entry, parent, false);
        return new WeeklyView(itemView);
    }

    public WeeklyWeatherAdapter(List<WeeklyWeather> weeklyWeatherItems, WeeklyWeatherActivity weeklyWeatherActivity) {
        this.weeklyWeatherItems = weeklyWeatherItems;
    }

    @Override
    public int getItemCount() {
        return weeklyWeatherItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyView holder, int position) {
        WeeklyWeather data = weeklyWeatherItems.get(position);
        holder.weekMorningTemp.setText(data.getWeekMorningTemp());
        holder.weekTemperature.setText(data.getWeekTemperature());
        holder.weekAfternoonTemp.setText(data.getWeekAfternoonTemp());
        holder.weekNightTemp.setText(data.getWeekNightTemp());
        holder.weekPrecipitation.setText(data.getWeekPrecipitation());
        holder.weekUvIndex.setText(data.getWeekUvIndex());
        holder.weekDateTimeEpoch.setText(data.getWeekDateTime());
        holder.weekEveningTemp.setText(data.getWeekEveningTemp());
        holder.weekDescription.setText(data.getWeekDescription());
        holder.weekImageView.setImageResource(data.getWeekImageView());
    }


}
