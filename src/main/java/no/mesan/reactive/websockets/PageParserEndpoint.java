package no.mesan.reactive.websockets;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import no.mesan.reactive.parser.PageParser;

import org.json.JSONObject;

/**
 * Handles websocket requests from frontend for pageparser.
 *
 * @author Mikkel Steine
 */
@ServerEndpoint(value = "/pageparser")
public class PageParserEndpoint {

    private static final Map<Session, PageParser> clients = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(final Session session) throws IOException, EncodeException {
        final PageParser client = new PageParser(this, session);
        clients.put(session, client);
    }

    @OnMessage
    public void onMessage(final String message, final Session session) throws IOException {
        final JSONObject json = new JSONObject(message);
        final String action = json.getString("action");
        if (action == null) {
            return;
        }

        final PageParser client = clients.get(session);
        if (action.equals("stop")) {
            client.stop();
        } else if (action.equals("parse")) {
            final String url = json.getString("url");
            client.parseURL(url);
        }
    }

    @OnClose
    public void onClose(final Session session, final CloseReason closeReason) {
        clients.remove(session);
    }

    public boolean sendStopped(final Session session) {
        return session != null && sendMessage(session, prepareMessageObject("stopped"));
    }

    private JSONObject prepareMessageObject(final String type) {
        final JSONObject json = new JSONObject();
        json.put("type", type);
        return json;
    }

    private boolean sendMessage(final Session session, final JSONObject json) {
        try {
            session.getBasicRemote().sendText(json.toString());
        } catch (final IOException e) {
            return false;
        }

        return true;
    }
}
