package com.varungupta.googleimagesearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by varungupta on 5/12/15.
 */
public class ImageResult {
    String url;

    public ImageResult(String url) {
        this.url = url;
    }

    public ImageResult(JSONObject json) {
        try {
            this.url = json.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static public ArrayList<ImageResult> ImageResults(JSONObject json) {
        ArrayList<ImageResult> imageResults = new ArrayList<>();
        try {
            JSONArray jsonArray = json.getJSONObject("responseData").getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                imageResults.add(new ImageResult(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return imageResults;
    }
}
