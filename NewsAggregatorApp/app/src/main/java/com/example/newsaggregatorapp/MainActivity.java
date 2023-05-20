package com.example.newsaggregatorapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsaggregatorapp.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {

    private DrawerLayout drawerLayout;
    private ActivityMainBinding binding;
    private NewsEntityActivity[] newsSources;
    private static final String TAG = "MainActivity";
    private ActionBarDrawerToggle drawerToggle;
    private String[] sources;
    public NewsEntityActivity newsEntityActivity;
    private static ArrayList<NewsEntityActivity> newsArticle = new ArrayList<>();
    Map<Integer, String> filterMap = new HashMap<>();
    private ArrayAdapter<String> arrayAdapter;
    ConstraintLayout frameContent;
    private ViewAdapter viewAdapter;
    RecyclerView recView;
    private Menu topics;
    private static List<NewsEntityActivity> news = new ArrayList<>();
    ArrayList<NewsEntityActivity> articles;
    private static RequestQueue queue, queue2;
    private String key ="3e5c3f7637334e6da6b58ab1c0de639a";
    private ListView drawerList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void modifyNewsArticle(String item, int id) {
        if (id == 0) return;
        String previous = filterMap.get(id);
        filterMap.put(id, item);

        NewsEntityActivity[] filteredSources = Arrays.stream(newsSources)
                .filter(source -> filterMap.get(1).equals("All") || source.getCategory().equals(filterMap.get(1)))
                .toArray(NewsEntityActivity[]::new);


        newsArticle = new ArrayList<>();
        Collections.addAll(newsArticle, filteredSources);
        String[] totalSources = null;
        totalSources = Arrays.stream(filteredSources).map(NewsEntityActivity::getName).toArray(String[]::new);

        if (totalSources.length == 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("No News Sources")
                    .setMessage("No news sources exist that match the specified resource")
                    .setPositiveButton("OK", (dialog, which) -> filterMap.put(id, previous))
                    .create();
            alertDialog.show();
        }
        else {
            sources = totalSources;
            arrayAdapter = new ArrayAdapter<String>(this, R.layout.drawer, sources) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView textView = view.findViewById(android.R.id.text1);
                    textView.setTextColor(newsEntityActivity.getColor(filteredSources[position].getCategory()));
                    return view;
                }

            };

            drawerList.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            setTitle("News Gateway (" + sources.length + ")");
            recView.setAdapter(null);
            frameContent.setBackgroundResource(R.drawable.background);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        topics = menu;
        topics.add(1, 0, 0, "All");
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: DrawerToggle " + item);
            return true;
        }
        modifyNewsArticle(item.getTitle().toString(), item.getGroupId());
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)

    private void menuUpgrade(NewsEntityActivity[] newsSources) {
        Arrays.stream(newsSources).map(NewsEntityActivity::getCategory).distinct().forEach((topic) -> {
            SpannableString str = new SpannableString(topic);
            str.setSpan(new ForegroundColorSpan(newsEntityActivity.getColor(topic)), 0, str.length(), 0);
            topics.add(1, 0, 0, str);
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void changeNewsFrom(List<NewsEntityActivity> newsSources) {
        int sourceLen = newsSources.size();
        newsArticle = new ArrayList<>(newsSources);
        sources = new String[newsSources.size()];
        int len = sources.length;

        for (int i = 0; i < len; i++)
            sources[i] = newsSources.get(i).getName();


        NewsEntityActivity[] newsData = new NewsEntityActivity[newsSources.size()];

        this.setTitle("News Gateway " + "(" +sources.length+")");
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.drawer, sources) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(newsEntityActivity.getColor(newsSources.get(position).getCategory()));
                return view;
            }

        };
        drawerList.setAdapter(arrayAdapter);

        int i = 0;
        while (i < sourceLen) {
            newsData[i] = newsSources.get(i);
            i++;
        }

        this.newsSources = newsData;
        menuUpgrade(newsData);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getNewsOrigin() {
        queue = Volley.newRequestQueue(this);
        Uri.Builder builder = Uri.parse("https://newsapi.org/v2/top-headlines/sources").buildUpon();
        builder.appendQueryParameter("apiKey", key);
        String url = builder.build().toString();

        Response.Listener<JSONObject> jsonObjectListener = response -> {
            try {
                JSONArray jSources = new JSONObject(response.toString()).getJSONArray("sources");

                int i = 0;
                while (i < jSources.length()) {
                    JSONObject News = (JSONObject) jSources.get(i);
                    news.add(new NewsEntityActivity(News.getString("id"), News.getString("name"), News.getString("category")));
                    i++;
                }
                changeNewsFrom(news);
            }catch (Exception e) {
                e.printStackTrace();
            }
        };



        Response.ErrorListener errorListener = error -> {
            changeNewsFrom(null);
        };
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,jsonObjectListener,errorListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "News-App");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void getNewsReport(String source){
        queue2 = Volley.newRequestQueue(this);
        Uri.Builder builder = Uri.parse("https://newsapi.org/v2/top-headlines").buildUpon();
        builder.appendQueryParameter("sources", source);
        builder.appendQueryParameter("apiKey", key);
        String url = builder.build().toString();

        Response.Listener<JSONObject> jsonObjectListener = response -> {
            articles = new ArrayList<>();
            try {
                JSONArray newsSources = new JSONObject(response.toString()).getJSONArray("articles");
                int sourceLen = newsSources.length();

                int i = 0;
                while (i < newsSources.length()) {
                    JSONObject article = (JSONObject) newsSources.get(i);
                    articles.add(new NewsEntityActivity(article.getString("title"), article.getString("author"), article.getString("description"),
                            article.getString("publishedAt"), article.getString("urlToImage"), article.getString("url")));
                    i++;
                }

                viewAdapter = new ViewAdapter(this);
                viewAdapter.changeNewsList(articles);
                recView.setAdapter(viewAdapter);
                recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            }catch (Exception e){
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {
            recView.setAdapter(viewAdapter);
            recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            return;
        };
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,jsonObjectListener,errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "News-App");
                return headers;
            }
        };
        queue2.add(jsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        startAppApi();
        drawerList.setOnItemClickListener((parent, view, position, id) -> {
            setTitle(newsArticle.get(position).getName());
            getNewsReport(newsArticle.get(position).getId());
            binding.contentFrame.setBackgroundColor(Color.WHITE);
            drawerLayout.closeDrawer(drawerList);
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startAppApi() {
        drawerLayout = binding.drawerLayout;
        drawerList = binding.drawerLeft;
        recView = binding.listNData;
        frameContent = binding.contentFrame;
        filterMap.put(1, "All");
        getNewsOrigin();
        newsEntityActivity = new NewsEntityActivity();
        newsEntityActivity.setColor(this);
    }
}
