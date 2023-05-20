package com.example.app_civiladvocacy;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private final List<OfficialActivity> offList = new ArrayList<>();
    private static final String TAG = "MainActivity";
    RecyclerView recyclerView;
    private static final int LOCATION_REQUEST = 111;
    private boolean locationInputUser = false;
    private OfficialAdapter offAdapter;
    private FusedLocationProviderClient fusedLocClient;
    private String userLocationInput = null;
    TextView textViewLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocClient =
                LocationServices.getFusedLocationProviderClient(this);
        determineLocation();

        recyclerView = findViewById(R.id.recycler);
        offAdapter = new OfficialAdapter(this, offList);
        recyclerView.setAdapter(offAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        textViewLocation = findViewById(R.id.locationText);

        if (!hasNetworkConnection()) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Know Your Government");
            ColorDrawable colorDrawable
                    = new ColorDrawable(Color.parseColor("#b3acbd")); //#A3A3A3
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
            textViewLocation.setText("No Data For Location");
            findViewById(R.id.noNetTitleText).setVisibility(View.VISIBLE);
            findViewById(R.id.noNetText).setVisibility(View.VISIBLE);
            findViewById(R.id.divider).setVisibility(View.VISIBLE);
            findViewById(R.id.divider2).setVisibility(View.VISIBLE);
            findViewById(R.id.divider3).setVisibility(View.VISIBLE);
            findViewById(R.id.divider4).setVisibility(View.VISIBLE);
            findViewById(R.id.recycler).setVisibility(View.INVISIBLE);
        }else{
            Objects.requireNonNull(getSupportActionBar()).setTitle("Civil Advocacy");
            ColorDrawable colorDrawable
                    = new ColorDrawable(Color.parseColor("#390c47"));
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
            findViewById(R.id.noNetTitleText).setVisibility(View.INVISIBLE);
            findViewById(R.id.noNetText).setVisibility(View.INVISIBLE);
            findViewById(R.id.divider).setVisibility(View.INVISIBLE);
            findViewById(R.id.divider2).setVisibility(View.INVISIBLE);
            findViewById(R.id.divider3).setVisibility(View.INVISIBLE);
            findViewById(R.id.divider4).setVisibility(View.INVISIBLE);
            findViewById(R.id.recycler).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("loc", textViewLocation.getText().toString());
        outState.putBoolean("userInput", locationInputUser);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String savedLocation = savedInstanceState.getString("location");
        boolean savedInput = savedInstanceState.getBoolean("userInput");
        if (hasNetworkConnection()) {
            textViewLocation.setText(savedLocation);
            locationInputUser = savedInput;
            getData();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuOptionAbout) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menuOptionLoc) {
            if (!hasNetworkConnection()) {
                Toast.makeText(this, "This app needs network connectivity to work", Toast.LENGTH_SHORT).show();
                return true;
            }
            final EditText et = new EditText(this);
            et.setInputType(InputType.TYPE_CLASS_TEXT);
            et.setGravity(Gravity.CENTER_HORIZONTAL);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(et);
            builder.setPositiveButton("OK", (dialogInterface, i) -> {
                textViewLocation.setText(et.getText());
                userLocationInput = et.getText().toString();
                locationInputUser = true;
                getData();
            });
            builder.setNegativeButton("CANCEL", (dialogInterface, i) -> {
            });
            builder.setTitle("Enter a Location");
            builder.setMessage("Enter either a 'City, State' or a ZIP code.");
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }



    private void determineLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }

        fusedLocClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null && hasNetworkConnection() && !locationInputUser) {
                        String locationString = getPlace(location);
                        Log.d(TAG, "determineLocation: " + locationString);
                        textViewLocation.setText(locationString);
                        getData();
                    }
                })
                .addOnFailureListener(this, e -> {
                    Toast.makeText(MainActivity.this,
                            e.getMessage(), Toast.LENGTH_LONG).show();
                    textViewLocation.setText("No Data For Location");
                });

    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }


    private String getPlace(Location loc) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String place = "";
        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                place += address.getAddressLine(0);
                if (address.getLocality() != null) {
                    place += ", " + address.getLocality();
                }
                if (address.getAdminArea() != null) {
                    place += ", " + address.getAdminArea();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return place;
    }

    private void getData() {
        Log.d(TAG, "getData: Pulling data via OfficialDownloader class");
        OfficialDownloader.downloadData(this, textViewLocation.getText().toString());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: User Denied Loc Permission");;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Log.d(TAG, "onClick: clicked on official " + pos);
        OfficialActivity official = offList.get(pos);
        Intent intent = new Intent(this, OfficialActivityView.class);
        intent.putExtra("DATA_OFFICIAL", official);
        intent.putExtra("DATA_LOC", textViewLocation.getText().toString());
        startActivity(intent);
    }



    public void updateData(List<OfficialActivity> officials, String address) throws JSONException {
        if (officials == null) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Know Your Government");
            textViewLocation.setText("No Data For Location");
            Toast.makeText(this, "Please Enter a Valid location Name", Toast.LENGTH_SHORT).show();
            return;
        }

        textViewLocation.setText(address);
        offList.clear();
        offList.addAll(officials);
        offAdapter.notifyDataSetChanged();
    }
}