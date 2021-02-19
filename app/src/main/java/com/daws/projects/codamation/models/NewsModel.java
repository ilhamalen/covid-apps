package com.daws.projects.codamation.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsModel {

    private int id;
    private String title;
    private String content;
    private String url;
    private String date;
    private int image;
    private String author;
    private String company;

    public NewsModel(Context context, JSONObject jsonObject){
        try {
            if (jsonObject.has("id"))
                setId(jsonObject.getInt("id"));
            if (jsonObject.has("title"))
                setTitle(jsonObject.getString("title"));
            if (jsonObject.has("content"))
                setContent(jsonObject.getString("content"));
            if (jsonObject.has("url"))
                setUrl(jsonObject.getString("url"));
            if (jsonObject.has("date"))
                setDate(jsonObject.getString("date"));
            if (jsonObject.has("image")){
                int imageRes = context.getResources().getIdentifier(jsonObject.getString("image"), "drawable", context.getPackageName());
                setImage(imageRes);
            }
            if (jsonObject.has("author"))
                setAuthor(jsonObject.getString("author"));
            if (jsonObject.has("company"))
                setCompany(jsonObject.getString("company"));
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
