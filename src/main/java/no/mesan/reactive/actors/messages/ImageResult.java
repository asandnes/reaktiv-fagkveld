package no.mesan.reactive.actors.messages;

import java.util.Arrays;

import org.json.JSONObject;

public class ImageResult {

    private final String imageUrl;
    private String thumbnailUrl;

    public ImageResult(final String imageUrl, final String thumbnailUrl) {
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String asJson() {
        final JSONObject json = prepareMessageObject("images");
        json.put("images", Arrays.asList(thumbnailUrl));
        return json.toString();
    }

    private JSONObject prepareMessageObject(final String type) {
        final JSONObject json = new JSONObject();
        json.put("type", type);
        return json;
    }
}
