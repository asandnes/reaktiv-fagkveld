package no.mesan.reactive.actors.messages;

import javax.websocket.Session;

public class ScanRequest {

    private final Session session;
    private final String url;

    public ScanRequest(final Session session, final String url) {
        this.session = session;
        this.url = url;
    }

    public Session getSession() {
        return session;
    }

    public String getUrl() {
        return url;
    }
}
