package com.example.app_civiladvocacy;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class OfficialActivityView extends AppCompatActivity {

    private static final String TAG = "ViewOfficialActivity";
    public OfficialActivity official;
    private String location;
    private TextView textViewLocation, name, title, party, address;

    private TextView phone, email, website, addressStatic, phoneStatic;

    private TextView emailStatic, websiteStatic;
    private ImageView faceBook, youTube, twitter;
    private ImageView partyImage, profileImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_official);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Civil Advocacy");
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#390c47"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        name = findViewById(R.id.OffViewNameText);
        title = findViewById(R.id.OffViewTittle);
        email = findViewById(R.id.textViewEmailVO);
        address = findViewById(R.id.textViewAddressVO);
        textViewLocation = findViewById(R.id.OffViewLocnText);
        website = findViewById(R.id.textViewWebsiteVO);
        phone = findViewById(R.id.textViewPhoneVO);
        party = findViewById(R.id.OffViewParty);
        faceBook = findViewById(R.id.FacbkImgView);
        twitter = findViewById(R.id.TwitImgView);
        youTube = findViewById(R.id.YTImgView);
        phoneStatic = findViewById(R.id.textViewPhoneVOStatic);
        emailStatic = findViewById(R.id.textViewEmailVOStatic);
        addressStatic = findViewById(R.id.textViewAddressVOStatic);
        partyImage = findViewById(R.id.imageViewParty);
        profileImage = findViewById(R.id.OffViewImg);
        websiteStatic = findViewById(R.id.textViewWebsiteVOStatic);

        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);

        Intent intent = getIntent();
        if (intent.hasExtra("DATA_OFFICIAL")) {
            Log.d(TAG, "onCreate: received official's data");
            official = (OfficialActivity) intent.getSerializableExtra("DATA_OFFICIAL");
            location = intent.getStringExtra("DATA_LOC");
            textViewLocation.setText(location);
            name.setText(official.getName());
            title.setText(official.getTitle());
            party.setText(String.format("(%s)", official.getParty()));
            if (official.getAddress() != null) {
                String officialAddress = official.getAddress();
                String officialCity = official.getCity();
                String officialState = official.getState();
                String officialZip = official.getZip();
                String addressDisplay = (officialAddress.equals("") ? "" : (officialAddress + "\n")) +
                        (officialCity.equals("") ? "" : (officialCity + ", ")) +
                        (officialState.equals("") ? "" : (officialState)) +
                        (officialZip.equals("") ? "" : (", " + officialZip));
                address.setText(addressDisplay);
                address.setPaintFlags(address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            } else {
                address.setVisibility(View.GONE);
                addressStatic.setVisibility(View.GONE);
            }
            if (official.getPhone() != null) {
                phone.setText(official.getPhone());
                phone.setPaintFlags(phone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            } else {
                phone.setVisibility(View.GONE);
                phoneStatic.setVisibility(View.GONE);
            }
            if (official.getEmail() != null) {
                email.setText(official.getEmail());
                email.setPaintFlags(email.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            } else {
                email.setVisibility(View.GONE);
                emailStatic.setVisibility(View.GONE);
            }
            if (official.getWebsite() != null) {
                website.setText(official.getWebsite());
                website.setPaintFlags(website.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            } else {
                website.setVisibility(View.GONE);
                websiteStatic.setVisibility(View.GONE);
            }
            if (official.getFbAccountLink() != null) {
                Log.d(TAG, "onCreate: Has FB account");
            } else {
                faceBook.setVisibility(View.INVISIBLE);
            }
            if (official.getTwitterAccountLink() != null) {
                Log.d(TAG, "onCreate: Has Twitter account");
            } else {
                twitter.setVisibility(View.INVISIBLE);
            }
            if (official.getYoutubeAccountLink() != null) {
                Log.d(TAG, "onCreate: Has YouTube account");
            } else {
                youTube.setVisibility(View.INVISIBLE);
            }
            if (official.getParty().equals("Republican Party")) {
                constraintLayout.setBackgroundColor(Color.RED);
                int iconID =
                        this.getResources().getIdentifier("rep_logo", "drawable", this.getPackageName());
                if (iconID == 0) {
                    Log.d(TAG, "onCreate: Couldn't get republican ico");
                }
                partyImage.setImageResource(iconID);

            } else if (official.getParty().equals("Democratic Party")) {
                constraintLayout.setBackgroundColor(Color.BLUE);
            } else {
                constraintLayout.setBackgroundColor(Color.BLACK);
                partyImage.setVisibility(View.INVISIBLE);
            }

            if (official.getPhotoURL() != null) {
                Log.d(TAG, "onBindViewHolder: getting - " + official.getPhotoURL());
                Picasso.get()
                        .load(official.getPhotoURL())
                        .error(R.drawable.brokenimage)
                        .into(profileImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "onSuccess: for " + official.getName());
                            }
                            @Override
                            public void onError(Exception e) {

                                Log.d(TAG, "onError: " + e);
                            }
                        });
            } else profileImage.setImageResource(R.drawable.missing);


        } else {
            Log.d(TAG, "onCreate: Data did not get passed as intent");
        }
    }

    public void onPhotoClick(View v) {
        Log.d(TAG, "onPhotoClick: Clicked on profile pic");
        Intent intent = new Intent(this, OfficialPhotoActivity.class);
        intent.putExtra("DATA_OFFICIAL", official);
        intent.putExtra("DATA_LOC", textViewLocation.getText().toString());
        if (official.getPhotoURL() != null) startActivity(intent);
    }

    public void onPartyClick(View v) {
        String partyLinkURL;
        switch (official.getParty()) {
            case "Democratic Party":
                partyLinkURL = "https://democrats.org/";
                break;
            case "Republican Party":
                partyLinkURL = "https://www.gop.com/";
                break;
            default:
                return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(partyLinkURL));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void clickFacebook(View v) {
        // You need the FB user's id for the url
        String fbLink;
        String fbUseLink;
        fbLink = "https://www.facebook.com/" + official.getFbAccountLink();

        Intent intent;
        fbUseLink = "fb://facewebmodal/f?href=" + fbLink;
        if (isPackageInstalled("com.facebook.katana")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fbUseLink));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fbLink));
        }

        PackageManager packageManager = getPackageManager();
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles facebook intents");
        }

    }


    public void youTubeClicked(View v) {
        String name;
        name = official.getYoutubeAccountLink();
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
        }
    }
    public void clickTwitter(View v) {
        String twit;
        twit = official.getTwitterAccountLink();
        String twitterAppUrl = "twitter://user?screen_name=" + twit;
        String twitterWebUrl = "https://twitter.com/" + twit;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (isPackageInstalled("com.twitter.android")) {
            intent.setData(Uri.parse(twitterAppUrl));
        } else {
            intent.setData(Uri.parse(twitterWebUrl));
        }

        PackageManager pm = getPackageManager();
        if (pm.resolveActivity(intent, 0) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No app can handle this action");
        }


    }

    public void clickCall(View v) {
        String phoneNumber;
        phoneNumber = official.getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));

        PackageManager packageManager = getPackageManager();
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent);
        } else {
            String errorMessage = "No app can handle this action";
            makeErrorAlert(errorMessage);
        }

    }



    public void clickEmail(View v) {
        String[] addresses = new String[]{official.getEmail()};
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);

        PackageManager pm = getPackageManager();
        if (pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No app can handle this action");
        }

    }


    public void clickMap(View v) {
        String mapAddress = address.getText().toString();

        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(mapAddress));

        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No app can handle this action");
        }
    }

    public void clickWebsite(View v) {
        String websiteURL = official.getWebsite();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteURL));
        PackageManager packageManager = getPackageManager();
        if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            startActivity(intent);
        }
    }


    public boolean isPackageInstalled(String packageName) {
        try {
            return getPackageManager().getApplicationInfo(packageName, 0).enabled;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void makeErrorAlert(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .create();
        alertDialog.show();
    }


}