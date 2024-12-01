package com.example.walksyncandroid.Models;

public class ActivityItem {
    private String title;
    private String value;

    public ActivityItem(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }
}
