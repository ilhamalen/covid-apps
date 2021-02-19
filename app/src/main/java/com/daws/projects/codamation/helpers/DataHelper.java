package com.daws.projects.codamation.helpers;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class DataHelper {

    private DataHelper(){}

    public static DataHelper newInstance(){
        DataHelper dataHelper = new DataHelper();

        return dataHelper;
    }

    public JSONArray loadJSONFromAsset(Context context, String assetName){
        String jsonString;
        JSONArray jsonData = new JSONArray();

        try {
            InputStream is = context.getAssets().open(assetName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
            try {
                jsonData = new JSONArray(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return jsonData;
    }
}
