package no.mesan.reactive.actors.messages;

public class ImageRequest {

    private final String imageUrl;

    public ImageRequest(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
