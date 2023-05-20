package com.example.app_civiladvocacy;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class OfficialPhotoActivity extends AppCompatActivity {
    private static final String TAG = "OfficialPhotoActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_photo);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Civil Advocacy");
        ColorDrawable colorD = new ColorDrawable(Color.parseColor("#390c47"));
        getSupportActionBar().setBackgroundDrawable(colorD);
        TextView textViewLocation = findViewById(R.id.locnText);
        TextView name = findViewById(R.id.nameTextV);
        TextView title = findViewById(R.id.titleText);
        ImageView profileImage = findViewById(R.id.imgViewv);
        ImageView partyImage = findViewById(R.id.imageViewPartyOP);
        ConstraintLayout constraintLayout = findViewById(R.id.ContraintLayoutOP);

        Intent intent = getIntent();
        if (intent.hasExtra("DATA_OFFICIAL")) {
            Log.d(TAG, "onCreate: received official's data");
            String location;
            OfficialActivity official = (OfficialActivity) intent.getSerializableExtra("DATA_OFFICIAL");
            location = intent.getStringExtra("DATA_LOC");
            textViewLocation.setText(location);
            name.setText(official.getName());
            title.setText(official.getTitle());


            int backgroundColor;
            int symID = 0;
            switch (official.getParty()) {
                case "Republican Party":
                    backgroundColor = Color.RED;
                    symID = this.getResources().getIdentifier("rep_logo", "drawable", this.getPackageName());
                    if (symID == 0) {
                        Log.d(TAG, "onCreate: Couldn't get republican icon");
                    }
                    break;
                case "Democratic Party":
                    backgroundColor = Color.BLUE;
                    break;
                default:
                    backgroundColor = Color.BLACK;
                    partyImage.setVisibility(View.INVISIBLE);
                    break;
            }
            constraintLayout.setBackgroundColor(backgroundColor);
            partyImage.setImageResource(symID);


            if (official.getPhotoURL() == null){
                profileImage.setImageResource(R.drawable.missing);
            }
            if (official.getPhotoURL() != null) {

                String photoUrl = official.getPhotoURL();
                Log.d(TAG, "Getting photo URL " + photoUrl);

                Picasso.get()
                        .load(photoUrl)
                        .error(R.drawable.brokenimage)
                        .into(profileImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                String name = official.getName();
                                Log.d(TAG, "Successfully loaded photo " + name);
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.d(TAG, "Error loading photo", e);
                            }
                        });

            }

        }
    }
}