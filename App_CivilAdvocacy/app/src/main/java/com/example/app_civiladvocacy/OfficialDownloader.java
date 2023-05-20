package com.example.app_civiladvocacy;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OfficialDownloader {

    private static final String TAG = "OfficialDownloader";
    private static MainActivity mainActivity;
    private static RequestQueue requestQueue;
    private static final String dataURL = "https://www.googleapis.com/civicinfo/v2/representatives";
    private static final String yourAPIKey ="AIzaSyDxs9bi5LE4fDqCQniKohRRDCaruORcntc";

    public static void downloadData(MainActivity mainActivityIn, String city) {

        mainActivity = mainActivityIn;
        requestQueue = Volley.newRequestQueue(mainActivity);
        String urlLinkk;
        Uri.Builder buildURL = Uri.parse(dataURL).buildUpon();
        buildURL.appendQueryParameter("key", yourAPIKey);
        buildURL.appendQueryParameter("address", city);
        urlLinkk = buildURL.build().toString();

        Response.Listener<JSONObject> listener = response -> parseJSON(response.toString());

        Response.ErrorListener error = error1 -> {
                    try {
                        mainActivity.updateData(null, null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlLinkk,
                        null, listener, error);
        requestQueue.add(jsonObjectRequest);
    }

    private static void parseJSON(String s) {
        List<OfficialActivity> officialList = new ArrayList<>();

        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONObject fullAddressJO = jObjMain.getJSONObject("normalizedInput");
            String line1 = fullAddressJO.getString("line1");
            String city = fullAddressJO.getString("city");
            String state = fullAddressJO.getString("state");
            String pinCode = fullAddressJO.getString("zip");
            StringBuilder strBuilder = new StringBuilder();
            if (!line1.isEmpty()) {
                strBuilder.append(line1).append(", ");
            }
            if (!city.isEmpty()) {
                strBuilder.append(city).append(", ");
            }
            if (!state.isEmpty()) {
                strBuilder.append(state);
            }
            if (!pinCode.isEmpty()) {
                strBuilder.append(", ").append(pinCode);
            }
            String address = strBuilder.toString();
            Log.d(TAG, "parseJSON: address is: " + address);

            // "offices" section
            JSONArray offices = jObjMain.getJSONArray("offices");
            JSONArray officialJA = jObjMain.getJSONArray("officials");
            int len = offices.length();
            for (int i = 0; i < len; i++) {
                String title = offices.getJSONObject(i).getString("name");
                JSONArray officials = offices.getJSONObject(i).getJSONArray("officialIndices");
                Log.d(TAG, "parseJSON: title: " + title);
                Log.d(TAG, "parseJSON: officials (index): " + officials);
                Log.d(TAG, "parseJSON: " + ("Building official data"));
                Log.d(TAG, "parseJSON: " + ("--------------------------"));
                int officialsLen = officials.length();
                int jk=0;
                while(jk<officialsLen) {

                        int officialIndex = officials.getInt(jk);
                        JSONObject officialJO = officialJA.getJSONObject(officialIndex);
                        String name = officialJO.getString("name");
                        String officialAddress = null, officialCity = null, officialState = null, officialZip = null;
                        String url = null, photoUrl = null, party = null, phone = null;
                        String email = null, twitterID = null, facebkID = null, youtubeID = null ;

                        if (officialJO.has("address")) {
                            officialAddress = "";
                            JSONObject officialAddressJO = officialJO.getJSONArray("address").getJSONObject(0);

                            officialAddress += officialAddressJO.optString("line1", "");
                            officialAddress += " " + officialAddressJO.optString("line2", "");
                            officialAddress += " " + officialAddressJO.optString("line3", "");

                            officialCity = officialAddressJO.optString("city", "");
                            officialState = officialAddressJO.optString("state", "");
                            officialZip = officialAddressJO.optString("zip", "");
                        }

                        if (officialJO.has("party")) party = officialJO.getString("party");
                        if (officialJO.has("phones"))
                            phone = officialJO.getJSONArray("phones").getString(0);
                        if (officialJO.has("urls")) url = officialJO.getJSONArray("urls").getString(0);
                        if (officialJO.has("emails"))
                        {email = officialJO.getJSONArray("emails").getString(0);}
                        if (officialJO.has("photoUrl")) photoUrl = officialJO.getString("photoUrl");

                        if (officialJO.has("channels")) {
                            JSONArray officialChannels = officialJO.getJSONArray("channels");
                            String channelType, channelID;
                            int var = 0;
                            int offChanLen = officialChannels.length();
                            while (var < offChanLen) {
                                channelType = officialChannels.getJSONObject(var).getString("type");
                                channelID = officialChannels.getJSONObject(var).getString("id");
                                switch (channelType) {
                                    case "Facebook":
                                        facebkID = channelID;
                                        break;
                                    case "Twitter":
                                        twitterID = channelID;
                                        break;
                                    case "YouTube":
                                        youtubeID = channelID;
                                        break;
                                }
                                var++;
                            }
                        }

                        OfficialActivity official = new OfficialActivity(name,
                                title,
                                party,
                                photoUrl,
                                facebkID,
                                twitterID,
                                youtubeID,
                                officialAddress,
                                officialCity,
                                officialState,
                                officialZip,
                                phone,
                                email,
                                url);

                        Log.d(TAG, "parseJSON: official: ");
                        officialList.add(official);

                    jk++;
                }
            }
            mainActivity.updateData(officialList, address);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
