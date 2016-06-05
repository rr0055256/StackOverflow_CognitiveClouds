package com.stackoverflow;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;

/**
 * Created by Angad on 17/04/16.
 */
//profile_image, display_name, score, last_activity_date, link, title,tags[]
public class Item implements Serializable {
    String profile_image;
    String display_name;
    int score;
    String link;
    String title;
    long last_activity_date;
    JSONArray tags=null;
    String favoriteTags=null;

    public Item(String image, String name, int score, String link, String title, long time, JSONArray tags,String favoriteTags) {
        this.profile_image = image;
        this.display_name = name;
        this.score = score;
        this.link = link;
        this.title = title;
        this.last_activity_date = time;
        this.tags = tags;
        this.favoriteTags=favoriteTags;
    }

    public Item(String pic, String name, int score, long time, String link, String title) {
        this.profile_image = pic;
        this.display_name = name;
        this.score = score;
        this.link = link;
        this.title = title;
        this.last_activity_date = time;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public int getScore() {
        return score;
    }

    public long getActivityLastDate() {

        return last_activity_date;
    }

    public String getTags() {
        String s = " ";
        for (int i = 0; i < tags.length(); i++) {
            try {
                s = s +" #"+tags.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    public String getFavoriteTags(){
        return favoriteTags;
    }
}


