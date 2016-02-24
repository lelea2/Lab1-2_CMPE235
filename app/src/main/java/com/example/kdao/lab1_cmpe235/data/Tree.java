package com.example.kdao.lab1_cmpe235.data;

/**
 * Tree class contains information about circuit tree
 */
public class Tree {
    String name;
    String description;
    String barcode;
    String url;
    int icon;
    Location location;

    public Tree(String description, String barcode, int icon, String url, Location location) {
        this.description = description;
        this.barcode = barcode;
        this.icon = icon;
        this.url = url;
        this.location = location;
    }

    //Set up getter, setter
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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
