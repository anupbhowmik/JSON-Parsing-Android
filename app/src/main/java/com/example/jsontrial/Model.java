package com.example.jsontrial;

public class Model {

    private final String imageResource;
    private final String userImageResource;
    private final String creator ;
    private final int likes;

    public String getUserImageResource() {
        return userImageResource;
    }

    public Model(String imageResource, String userImageResource, String creator, int likes) {
        this.imageResource = imageResource;
        this.userImageResource = userImageResource;
        this.creator = creator;
        this.likes = likes;
    }

    public String getImageResource() {
        return imageResource;
    }

    public String getCreator() {
        return creator;
    }

    public int getLikes() {
        return likes;
    }
}
