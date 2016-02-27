package com.example.kdao.lab1_cmpe235.data;

/**
 * Tree class contains information about circuit tree
 */
public class Tree {
    String name;
    String description;
    String url;
    int icon;
    Location location;

    public Tree(String description, int icon, String url, Location location) {
        this.description = description;
        this.icon = icon;
        this.url = url;
        this.location = location;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
