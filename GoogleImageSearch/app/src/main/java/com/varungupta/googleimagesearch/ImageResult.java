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
    String thumbnailUrl;

    public ImageResult(JSONObject json) {
        try {
            this.url = json.getString("url");
            this.thumbnailUrl = json.getString("tbUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static public ArrayList<ImageResult> ImageResults(JSONObject json) {
        /*
        {
            responseData: {
                results: [
                    {
                        GsearchResultClass: "GimageSearch",
                        width: "2956",
                        height: "2000",
                        imageId: "ANd9GcQGUmNpgU3iH6wod2p5ooaJkQlvAVLMmc4f1olZPp9A8nWX8HM9vTdPdwg",
                        tbWidth: "150",
                        tbHeight: "101",
                        unescapedUrl: "http://www.grandfather.com/wp-content/uploads/2012/08/fall-road.jpg",
                        url: "http://www.grandfather.com/wp-content/uploads/2012/08/fall-road.jpg",
                        visibleUrl: "www.grandfather.com",
                        title: "Hi Res Photos-",
                        titleNoFormatting: "Hi Res Photos-",
                        originalContextUrl: "http://www.grandfather.com/about-grandfather-mountain/media/hi-res-photos-fall/",
                        content: "<b>fall</b>-road.jpg",
                        contentNoFormatting: "fall-road.jpg",
                        tbUrl: "http://t2.gstatic.com/images?q=tbn:ANd9GcQGUmNpgU3iH6wod2p5ooaJkQlvAVLMmc4f1olZPp9A8nWX8HM9vTdPdwg"
                    },
                    .
                    .
                    .
                ],
                cursor: {
                    resultCount: "326,000,000",
                    pages: [
                        {
                            start: "0",
                            label: 1
                        },
                        {
                            start: "4",
                            label: 2
                        },
                        .
                        .
                        .
                    ],
                    estimatedResultCount: "326000000",
                    currentPageIndex: 0,
                    moreResultsUrl: "http://www.google.com/images?oe=utf8&ie=utf8&source=uds&start=0&hl=en&q=fall",
                    searchResultTime: "0.37"
                }
            },
            responseDetails: null,
            responseStatus: 200
        }
        */

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
