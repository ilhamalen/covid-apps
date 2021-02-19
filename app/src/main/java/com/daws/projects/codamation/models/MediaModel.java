package com.daws.projects.codamation.models;

import com.daws.projects.codamation.helpers.DateHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MediaModel {

    private String id;
    private String title;
    private String publishedDate;
    private String thumbnailUrl;
    private String videoUrl;

    public MediaModel(JSONObject responseObject){
        try {
            if (responseObject.has("id"))
                setId(responseObject.getString("id"));
            if (responseObject.has("title"))
                setTitle(responseObject.getString("title"));
            if (responseObject.has("publishedDate")){
                String originalFormatDate = responseObject.getString("publishedDate");
                try {
                    setPublishedDate(DateHelper.getInstance().convertUTCToLongDateTimeWithMonthName(originalFormatDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                    setPublishedDate("");
                }
            }
            if (responseObject.has("thumbnailUrl"))
                setThumbnailUrl(responseObject.getString("thumbnailUrl"));
            if (responseObject.has("videoUrl"))
                setVideoUrl(responseObject.getString("videoUrl"));
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
