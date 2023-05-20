package com.example.app_civiladvocacy;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.util.Objects;

public class AboutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        setContentView(R.layout.activity_about);
    }

    private void setupActionBar() {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Civil Advocacy");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#390c47")));
    }

    public void clickGoogleLink(View v) {
        String GoogleUrl;
        GoogleUrl = "https://developers.google.com/civic-information/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GoogleUrl));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}