package com.varungupta.googleimagesearch;

import android.net.Uri;

/**
 * Created by varungupta on 5/13/15.
 */
public class SearchSettings {
    public String imageType;
    public String imageColor;
    public String imageSize;
    public String website;

    private static SearchSettings instance = new SearchSettings();

    private SearchSettings() {
        imageColor = null;
        imageSize = null;
        imageType = null;
        website = null;
    }

    public static SearchSettings getInstance() {
        return instance;
    }

    public void saveSettings(String imageSize, String imageColor, String imageType, String website) {
        this.imageType = imageType;
        this.imageSize = imageSize;
        this.imageColor = imageColor;
        this.website = website;
    }

    public void appendParams(Uri.Builder builder) {
        if (imageType != null && !imageType.equals("any")) {
            builder.appendQueryParameter("imgtype", imageType);
        }

        if (imageColor != null && !imageColor.equals("any")) {
            builder.appendQueryParameter("imgcolor", imageColor);
        }

        if (imageSize != null && !imageSize.equals("any")) {
            builder.appendQueryParameter("imgsz", imageSize);
        }

        if (website != null && !imageType.trim().equals("")) {
            builder.appendQueryParameter("as_sitesearch", website);
        }
    }
}
