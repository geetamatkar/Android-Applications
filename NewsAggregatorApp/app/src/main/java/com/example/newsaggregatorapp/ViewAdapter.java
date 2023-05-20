package com.example.newsaggregatorapp;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewAdapter extends RecyclerView.Adapter<ViewHolder>{
    MainActivity mainActivity;
    ArrayList<NewsEntityActivity> newsEntityActivityList;

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.news_page, parent, false));
    }

    public void changeNewsList(ArrayList<NewsEntityActivity> newsEntityActivityList){
        this.newsEntityActivityList = newsEntityActivityList;
    }

    @Override
    public int getItemCount() {
        return newsEntityActivityList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NewsEntityActivity newsEntityActivity = newsEntityActivityList.get(position);
        Date date = null;
        try {
            date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(newsEntityActivity.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateTimeFormat = (new SimpleDateFormat("MMM dd, yyyy hh:mm")).format(date);

        holder.img.setOnClickListener(v -> this.mainActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(newsEntityActivity.getNewsUrl()))));
        holder.description.setOnClickListener(v -> this.mainActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(newsEntityActivity.getNewsUrl()))));
        holder.heading.setOnClickListener(v -> this.mainActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(newsEntityActivity.getNewsUrl()))));


        holder.heading.setText(newsEntityActivity.getTitle() == null ? "" : newsEntityActivity.getTitle());
        holder.description.setText(newsEntityActivity.getDesc() == null ? "" : newsEntityActivity.getDesc());
        holder.author.setText(newsEntityActivity.getAuthor() == null ? "" : newsEntityActivity.getAuthor());

        holder.time.setText(dateTimeFormat);
        holder.totalPages.setText((position + 1) + " of " + newsEntityActivityList.size());


        String urlImage = newsEntityActivity.getUrlImage();
        if (urlImage != null && !urlImage.equals("null")) {
            holder.img.setImageResource(R.drawable.loading);
            new ImageLoad(holder.img, mainActivity).execute(urlImage);
        }

    }

    public ViewAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

}
