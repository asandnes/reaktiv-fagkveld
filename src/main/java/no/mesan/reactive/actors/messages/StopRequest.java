package no.mesan.reactive.actors.messages;

import javax.websocket.Session;

public class StopRequest {

    private final Session session;

    public StopRequest(final Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }
}
