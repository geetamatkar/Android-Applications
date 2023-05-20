package com.example.weatherapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private String presentLocation, tempUnit;
    private static final String weatherURL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    private static final String myAPIKey = "TXALXH8LRV8QYQ8MCLR439U4A";
    SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPref;
    private RequestQueue requestQueue;
    private ImageView presentImage;
    private TextView bannerText, presentTemp, feelsLikeTemperature, presentCondition;

    private TextView presWinDirn, presHumidity, presentUVIndex, presVisible;

    private TextView presentSunRise, presentSunSet;
    private TextView presentMorningWeather, presentAfternoonWeather, presentEveningWeather, presentNightWeather;
    private CurrentWeatherAdapter currentWeatherAdapter;
    private LinearLayout dayTempLinearlayout;
    private RecyclerView recyclerView;
    private JSONObject cachedAPIResponse = null;
    MenuItem menuItem;
    private final List<CurrentWeather> weatherItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pastData();
        defaultActionBar();
        defaultView();
        defaultHorizonView();

        requestQueue = Volley.newRequestQueue(this);
        if(hasNetworkConnection()){           //check internet connection
            getTimelineWeatherData();
        }
        else{
            bannerText.setText(R.string.noInternetText);
            bannerText.setVisibility(View.VISIBLE);
        }
    }

    private void pastData() {
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        presentLocation = sharedPref.getString("currentLocation", "Chicago, IL");
        tempUnit = sharedPref.getString("temperatureUnit", "us");

    }

    private void defaultHorizonView(){
        recyclerView = findViewById(R.id.homeRecycler);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        currentWeatherAdapter = new CurrentWeatherAdapter(weatherItems, this);
        recyclerView.setAdapter(currentWeatherAdapter);
    }

    private void defaultActionBar(){
        setTitle("Weather App");
        ActionBar actBar = getSupportActionBar();
        ColorDrawable colorDraw = new ColorDrawable(Color.parseColor("#61553f"));
        if(actBar != null){
            actBar.setBackgroundDrawable(colorDraw);
        }
    }

    private void defaultView(){

        refreshLayout = findViewById(R.id.swiperefresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(hasNetworkConnection()){
                    getTimelineWeatherData();               
                }else{
                    Toast.makeText(MainActivity.this, "No Internet!", Toast.LENGTH_SHORT).show();
                }
                refreshLayout.setRefreshing(false);
            }
        });

        bannerText = findViewById(R.id.bannerText);
        presentCondition= findViewById(R.id.presentCondition);
        feelsLikeTemperature= findViewById(R.id.presentFeelsTemp);
        presHumidity= findViewById(R.id.presHumidity);
        presentTemp= findViewById(R.id.presentTemp);
        presentUVIndex= findViewById(R.id.presentUVIndex);
        presWinDirn= findViewById(R.id.presWinDirn);
        presentImage= findViewById(R.id.presentImage);
        presVisible= findViewById(R.id.presVisible);
        presentMorningWeather= findViewById(R.id.presentMorningWeather);
        presentAfternoonWeather= findViewById(R.id.presentAfternoonWeather);
        presentEveningWeather= findViewById(R.id.presentEveningWeather);
        presentNightWeather= findViewById(R.id.presentNightWeather);
        presentSunRise= findViewById(R.id.presentSunRise);
        presentSunSet= findViewById(R.id.presentSunSet);
        dayTempLinearlayout = findViewById(R.id.dayTempLinearlayout);
    }

    private String getDateString(Long e){
        Date date = new Date(e * 1000);
        SimpleDateFormat simDateForm = new SimpleDateFormat("EEE MMM dd h:mm a, yyyy", Locale.getDefault());
        return simDateForm.format(date);
    }

    private String getLocationWeather(String presentL){
        Uri.Builder buildURL = Uri.parse(weatherURL + presentL).buildUpon();
        buildURL.appendQueryParameter("unitGroup", tempUnit);
        buildURL.appendQueryParameter("lang", "en");
        buildURL.appendQueryParameter("key", myAPIKey);
        return buildURL.build().toString();
    }

    private Integer getIconId(String string){
        String dash = string.replace("-", "_"); // Replace all dashes with underscores
        int dashId = MainActivity.this.getResources().getIdentifier(dash, "drawable", MainActivity.this.getPackageName());
        if (dashId == 0) {
            Log.d(TAG, "parseCurrentRecord: CANNOT FIND ICON " + dash);
        }
        return dashId;
    }

    private String getTimeString(Long e){
        Date date = new Date(e * 1000);
        SimpleDateFormat dayTime = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return dayTime.format(date);
    }

    private String getDayData(Long e){
        Date date = new Date(e * 1000);
        SimpleDateFormat dayDate = new SimpleDateFormat("EEEE", Locale.getDefault());
        return dayDate.format(date);
    }

    private String getWindUnit(){
        return (tempUnit.equals("us") ? "mph" : "km/h");
    }

    private String temperatureScale(){
        return "°" + (tempUnit.equals("us") ? "F" : "C");
    }

    private String visibleUnit(){
        return (tempUnit.equals("us") ? "mi" : "km");
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    private void updateView(JSONObject json){
        try {

            String locn = json.getString("address");
            setTitle(locn);

            presentLocation = locn;

            sharedPref.edit().putString("currentLocation", locn).apply();

            JSONObject currentConditions = json.getJSONObject("currentConditions");

            Long dateTimeEpoch = currentConditions.getLong("datetimeEpoch");
            bannerText.setText(getDateString(dateTimeEpoch));
            bannerText.setVisibility(View.VISIBLE);

            String unit = temperatureScale();

            presentTemp.setText(currentConditions.getString("temp") + unit);
            presentTemp.setVisibility(View.VISIBLE);

            feelsLikeTemperature.setText("Feels like " + currentConditions.getString("feelslike") + unit);
            feelsLikeTemperature.setVisibility(View.VISIBLE);

            presentCondition.setText(currentConditions.getString("conditions") + " (" + currentConditions.getInt("cloudcover") + "% clouds)");
            presentCondition.setVisibility(View.VISIBLE);

            String windGustUnit = getWindUnit();
            String gust = currentConditions.getString("windgust").equals("null") ? "0" : currentConditions.getString("windgust");

            presWinDirn.setText("Winds: " + getWindDirection(currentConditions.getLong("winddir")) + " at " + currentConditions.getString("windspeed") + " " + windGustUnit +" gusting to " + gust + " " + windGustUnit);
            presWinDirn.setVisibility(View.VISIBLE);

            presHumidity.setText("Humidity: " + currentConditions.getString("humidity") + "%");
            presHumidity.setVisibility(View.VISIBLE);

            presentUVIndex.setText("UV Index: " + currentConditions.getString("uvindex"));
            presentUVIndex.setVisibility(View.VISIBLE);

            presentImage.setImageResource(getIconId(currentConditions.getString("icon")));
            presentImage.setVisibility(View.VISIBLE);

            presVisible.setText("Visibility: " + currentConditions.getString("visibility") + " " + visibleUnit());
            presVisible.setVisibility(View.VISIBLE);

            presentSunRise.setText("Sunrise: " + getTimeString(currentConditions.getLong("sunriseEpoch")));
            presentSunRise.setVisibility(View.VISIBLE);

            presentSunSet.setText("Sunset: " + getTimeString(currentConditions.getLong("sunsetEpoch")));
            presentSunSet.setVisibility(View.VISIBLE);

            JSONArray days = json.getJSONArray("days");
            JSONArray hours = ((JSONObject) days.get(0)).getJSONArray("hours");

            presentMorningWeather.setText(((JSONObject) hours.get(8)).getString("temp") + unit);
            presentMorningWeather.setVisibility(View.VISIBLE);

            presentAfternoonWeather.setText(((JSONObject) hours.get(13)).getString("temp") + unit);
            presentAfternoonWeather.setVisibility(View.VISIBLE);

            presentEveningWeather.setText(((JSONObject) hours.get(17)).getString("temp") + unit);
            presentEveningWeather.setVisibility(View.VISIBLE);

            presentNightWeather.setText(((JSONObject) hours.get(23)).getString("temp") + unit);
            presentNightWeather.setVisibility(View.VISIBLE);

            dayTempLinearlayout.setVisibility(View.VISIBLE);

            // Reset previous data
            if(weatherItems.size() > 0){
                weatherItems.clear();
            }

            for(int i = 0; i<3 ; i++) {
                JSONArray j_hours = ((JSONObject) days.get(i)).getJSONArray("hours");
                for (int j = 0; j < 24; j++) {
                    String day = ((JSONObject) j_hours.get(j)).getString("datetimeEpoch");
                    String temp = ((JSONObject) j_hours.get(j)).getString("temp");
                    String icon = ((JSONObject) j_hours.get(j)).getString("icon");
                    String conditions = ((JSONObject) j_hours.get(j)).getString("conditions");
                    CurrentWeather newWeather = new CurrentWeather(i==0 ? "Today"  : getDayData(Long.parseLong(day)), getTimeString(Long.parseLong(day)), getIconId(icon), temp + unit, conditions);
                    weatherItems.add(newWeather);
                }
            }
            currentWeatherAdapter.notifyDataSetChanged();

            Log.d(TAG, "onResponse: days: " + days.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void getTimelineWeatherData(){
        Log.d(TAG, "getTimelineWeatherData: ");
        Response.Listener<JSONObject> listener =
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            cachedAPIResponse = response;
                            runOnUiThread(() -> updateView(response));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                        }
                    }
                };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                    Toast.makeText(MainActivity.this, jsonObject.toString(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        String updateUrl = getLocationWeather(presentLocation);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, updateUrl, null, listener, error);

        requestQueue.add(jsonObjectRequest);
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectiv = getSystemService(ConnectivityManager.class);
        NetworkInfo netInfo = connectiv.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    private String getWindDirection(double degreeTemp) {
        if (degreeTemp >= 337.5 || degreeTemp < 22.5) return "N";
        if (degreeTemp >= 22.5 && degreeTemp < 67.5) return "NE";
        if (degreeTemp >= 67.5 && degreeTemp < 112.5) return "E";
        if (degreeTemp >= 112.5 && degreeTemp < 157.5) return "SE";
        if (degreeTemp >= 157.5 && degreeTemp < 202.5) return "S";
        if (degreeTemp >= 202.5 && degreeTemp < 247.5) return "SW";
        if (degreeTemp >= 247.5 && degreeTemp < 292.5) return "W";
        if (degreeTemp >= 292.5 && degreeTemp < 337.5) return "NW";
        return "X";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        if(Objects.equals(tempUnit, "us")){
            menuItem.setIcon(R.drawable.units_f);
        }else{
            menuItem.setIcon(R.drawable.units_c);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: " + item.getItemId());
        if(!hasNetworkConnection()){
            Toast.makeText(MainActivity.this, "This Feature requires an active internet connection to work!", Toast.LENGTH_LONG).show();
            return super.onOptionsItemSelected(item);
        }
        if(item.getItemId() == R.id.menuA){

            if(Objects.equals(tempUnit, "us")){
                tempUnit = "metric";
                item.setIcon(R.drawable.units_c);
            }else{
                tempUnit = "us";
                item.setIcon(R.drawable.units_f);
            }

            sharedPref.edit().putString("temperatureUnit", tempUnit).apply();
            getTimelineWeatherData();
        }else if(item.getItemId() == R.id.menuB){
            Intent intent = new Intent(MainActivity.this, WeeklyWeatherActivity.class);
            intent.putExtra("TITLE", presentLocation);
            intent.putExtra("UNIT", tempUnit);
            intent.putExtra("DATA", cachedAPIResponse.toString());
            startActivity(intent);
        }else if(item.getItemId() == R.id.menuC){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter a location");
            builder.setMessage("For US locations, enter as 'City', or 'City,State' \n\nFor international locations enter as 'City,Country'");

            final EditText locationName = new EditText(this);
            locationName.setInputType(InputType.TYPE_CLASS_TEXT);
            locationName.setGravity(Gravity.CENTER_HORIZONTAL);
            builder.setView(locationName);
            builder.setPositiveButton("OK", (dialog, id) -> {


                String selectLocation = locationName.getText().toString();

                Pattern international = Pattern.compile("^[a-zA-Z]+(?:,[ ]?[a-zA-Z]+)+$");
                Pattern usa = Pattern.compile("^[a-zA-Z]+(?:,[ ]?[a-zA-Z]+)?$");

                Matcher internationalMatcher = international.matcher(selectLocation);
                Matcher usaMatcher = usa.matcher(selectLocation);

                if (usaMatcher.matches() || internationalMatcher.matches()) {
                    presentLocation = selectLocation;
                    getTimelineWeatherData();
                } else{
                    Toast.makeText(MainActivity.this, "Invalid location format!", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("CANCEL", (dialog, id) -> {});
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            Toast.makeText(this, "Invalid Menu Item!", Toast.LENGTH_LONG).show();
            Log.d(TAG, "onOptionsItemSelected: Invalid Menu Item Reference!");
        }

        return super.onOptionsItemSelected(item);
    }
}