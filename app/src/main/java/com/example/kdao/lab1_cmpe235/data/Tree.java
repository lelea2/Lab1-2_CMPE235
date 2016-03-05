package com.example.kdao.lab1_cmpe235.data;

/**
 * Tree class contains information about circuit tree
 */
public class Tree {
    String name;
    String description;
    String videoId;
    int icon;
    Location location;

    /**
     * Constructor of tree class
     * @param description
     * @param icon
     * @param videoId
     * @param location
     */
    public Tree(String description, int icon, String videoId, Location location) {
        this.description = description;
        this.icon = icon;
        this.videoId = videoId;
        this.location = location;
    }

    //Getter & Setter method
    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
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
