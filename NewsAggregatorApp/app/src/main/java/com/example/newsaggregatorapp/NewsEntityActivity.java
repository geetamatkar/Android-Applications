package com.example.newsaggregatorapp;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

public class NewsEntityActivity {

    String name, id, category, title, author;
    private Map<String, Integer> colors;
    String newsUrl, urlImage, description, time;

    @Override
    public String toString() {
        return "NewsEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", colors=" + colors +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", time='" + time + '\'' +
                ", newsUrl='" + newsUrl + '\'' +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }

    public NewsEntityActivity() {
    }

    public NewsEntityActivity(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }
    public NewsEntityActivity(String title, String author, String description, String time, String urlImage, String newsUrl) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.time = time;
        this.newsUrl = newsUrl;
        this.urlImage = urlImage;
    }
    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
    public String getCategory() {
        return category;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDesc(String description) {
        this.description = description;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public Map<String, Integer> getColors() {
        return colors;
    }
    public void setColor(MainActivity mainActivity) {
        colors = new HashMap<>();
        Resources res = mainActivity.getResources();

        colors.put("business", res.getColor(R.color.business));
        colors.put("entertainment", res.getColor(R.color.entertainment));
        colors.put("general", res.getColor(R.color.general));
        colors.put("health", res.getColor(R.color.health));
        colors.put("science", res.getColor(R.color.science));
        colors.put("sports", res.getColor(R.color.sports));
        colors.put("technology", res.getColor(R.color.technology));
    }

    public int getColor(String type) {
        return colors.get(type);
    }
    public String getNewsUrl() {
        return newsUrl;
    }
    public String getUrlImage() {
        return urlImage;
    }
    public String getTime() {
        return time;
    }
    public String getDesc() {
        return description;
    }

}
