package no.mesan.reactive.actors;

import java.io.IOException;

import javax.websocket.Session;

import no.mesan.reactive.actors.messages.ImageResult;
import no.mesan.reactive.actors.messages.ScanMessage;
import no.mesan.reactive.actors.messages.StopRequest;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class KlientAnsvarlig extends AbstractActor {

    private final Session session;
    private final String domain;

    private final ActorRef sideAnsvarlig;

    public KlientAnsvarlig(final Session session, final String domain) {
        this.session = session;
        this.domain = domain;
        final ActorRef bildeAnsvarlig = context().actorOf(Props.create(BildeAnsvarlig.class, context().self()));
        sideAnsvarlig = context().actorOf(Props.create(SideAnsvarlig.class, bildeAnsvarlig, this.domain));

        receive(ReceiveBuilder
          .match(ScanMessage.class, this::handleScanMessage)
          .match(ImageResult.class, this::handleImageResult)
          .match(StopRequest.class, this::handleStopRequest)
          .matchAny((Object message) -> {
              unhandled(message);
          }).build()
        );
    }

    private void handleScanMessage(final ScanMessage scanMessage) {
        System.out.println("Starter scanning av " + scanMessage.getUrl());
        sideAnsvarlig.tell(scanMessage, context().self());
    }

    private void handleImageResult(final ImageResult imageResult) throws IOException {
        session.getBasicRemote().sendText(imageResult.asJson());
    }

    private void handleStopRequest(final StopRequest stopRequest) {
        System.out.println("Stopper scanning av " + domain);
        context().stop(context().self());
    }
}
