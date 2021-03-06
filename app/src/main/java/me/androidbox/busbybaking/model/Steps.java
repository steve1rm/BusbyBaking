package me.androidbox.busbybaking.model;

import org.parceler.Parcel;

/**
 * Created by steve on 5/24/17.
 */
@Parcel
public class Steps {
    int id;
    String shortDescription;
    String videoURL;
    String thumbnailURL;

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
}
