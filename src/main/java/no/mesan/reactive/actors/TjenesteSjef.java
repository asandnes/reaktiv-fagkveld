package no.mesan.reactive.actors;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

import no.mesan.reactive.actors.messages.ScanMessage;
import no.mesan.reactive.actors.messages.ScanRequest;
import no.mesan.reactive.actors.messages.StopRequest;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class TjenesteSjef extends AbstractActor {

    private final Map<Session, ActorRef> handlers = new HashMap<>();

    public TjenesteSjef() {
        receive(ReceiveBuilder
          .match(ScanRequest.class, this::handleScanRequest)
          .match(StopRequest.class, this::handleStopRequest)
          .matchAny((Object unknownMessage) -> {
              unhandled(unknownMessage);
          }).build()
        );
    }

    private void handleScanRequest(final ScanRequest scanRequest) throws URISyntaxException {
        final String domain = getDomainFromUrl(scanRequest.getUrl());
        final Props props = Props.create(KlientAnsvarlig.class, scanRequest.getSession(), domain);
        final ActorRef klientAnsvarlig = context().actorOf(props);
        handlers.put(scanRequest.getSession(), klientAnsvarlig);
        klientAnsvarlig.tell(new ScanMessage(scanRequest.getUrl()), context().self());
    }

    private void handleStopRequest(final StopRequest stopRequest) {
        final ActorRef klientAnsvarlig = handlers.get(stopRequest.getSession());
        klientAnsvarlig.tell(stopRequest, context().self());
    }

    private String getDomainFromUrl(final String url) throws URISyntaxException {
        final URI uri = new URI(url);
        final String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}
