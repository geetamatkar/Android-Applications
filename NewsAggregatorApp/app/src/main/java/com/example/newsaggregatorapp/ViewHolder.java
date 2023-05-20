package com.example.newsaggregatorapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView heading, description, totalPages;
        TextView author, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.NewsAuthor);
            time = itemView.findViewById(R.id.NewsDate);
            heading = itemView.findViewById(R.id.NewsHeading);
            description = itemView.findViewById(R.id.Data);
            totalPages = itemView.findViewById(R.id.totalNumber);
            img = itemView.findViewById(R.id.NewsImg);

        }
}
