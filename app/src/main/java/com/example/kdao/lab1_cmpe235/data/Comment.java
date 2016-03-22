package com.example.kdao.lab1_cmpe235.data;

/**
 * Create comment class
 */
public class Comment {
    String comment;
    String username;
    int rating;

    /**
     * Get rating
     * @return
     */
    public int getRating() {
        return rating;
    }

    /**
     * Set rating
     * @param rating
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Get comment
     * @return
     */
    public String getComment() {
        return comment;
    }

    /**
     * Set comment
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Get username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
