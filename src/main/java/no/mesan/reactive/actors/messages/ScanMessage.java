package no.mesan.reactive.actors.messages;

public class ScanMessage {

    private final String url;

    public ScanMessage(final String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
