package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CurrentWeatherAdapter extends RecyclerView.Adapter<CurrentWeatherView> {
    private final List<CurrentWeather> weatherItems;
    private static final String TAG = "DailyWeatherAdapter";

    @NonNull
    @Override
    public CurrentWeatherView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_weather, parent, false);
        return new CurrentWeatherView(itemView);
    }

    public CurrentWeatherAdapter(List<CurrentWeather> currentWeatherItems, MainActivity mainActivity) {
        this.weatherItems = currentWeatherItems;
    }

    @Override
    public int getItemCount() {
        return weatherItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentWeatherView holder, int position) {
        CurrentWeather data = weatherItems.get(position);
        holder.image.setImageResource(data.getImage());
        holder.day.setText(data.getDay());
        holder.temperature.setText(data.getTemper());
        holder.dateTime.setText(data.getDateTime());
        holder.constrains.setText(data.getConstrains());
    }


}
