package no.mesan.reactive.parser;

import javax.websocket.Session;

import no.mesan.reactive.actors.TjenesteSjef;
import no.mesan.reactive.actors.messages.ScanRequest;
import no.mesan.reactive.actors.messages.StopRequest;
import no.mesan.reactive.websockets.PageParserEndpoint;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * Handles page parser requests and sends results trough websocket (PageParserEndpoint).
 *
 * @author Mikkel Steine
 */
public class PageParser {

    private final PageParserEndpoint endpoint;
    private final Session session;
    private final ActorRef tjenesteSjef;

    public PageParser(final PageParserEndpoint endpoint, final Session session) {
        this.endpoint = endpoint;
        this.session = session;
        final ActorSystem actorSystem = ActorSystem.create();
        tjenesteSjef = actorSystem.actorOf(Props.create(TjenesteSjef.class), "tjenestesjef");
    }

    public void parseURL(final String url) {
        tjenesteSjef.tell(new ScanRequest(session, url), null);
    }

    public void stop() {
        tjenesteSjef.tell(new StopRequest(session), null);
        // Send message to frontend that we have stopped.
        endpoint.sendStopped(session);
    }
}
