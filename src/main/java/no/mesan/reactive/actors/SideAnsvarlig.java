package no.mesan.reactive.actors;

import java.util.HashSet;
import java.util.Set;

import no.mesan.reactive.actors.messages.ScanMessage;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class SideAnsvarlig extends AbstractActor {

    private final String domain;
    private Set<String> foundPages = new HashSet<>();
    private ActorRef bildeAnsvarlig;

    public SideAnsvarlig(final ActorRef bildeAnsvarlig, final String domain) {
        this.bildeAnsvarlig = bildeAnsvarlig;
        this.domain = domain;

        receive(ReceiveBuilder
          .match(ScanMessage.class, this::handleScanMessage)
          .matchAny((Object message) -> {
              unhandled(message);
          }).build()
        );
    }

    private void handleScanMessage(final ScanMessage scanMessage) {
        final String url = scanMessage.getUrl();
        if (notVisitedYet(url) && isInSameDomain(url)) {
            final ActorRef sideLeser = context().actorOf(Props.create(SideLeser.class, bildeAnsvarlig));
            sideLeser.tell(scanMessage, context().self());
        }
        foundPages.add(url);
    }

    private boolean notVisitedYet(final String url) {
        return !foundPages.contains(url);
    }

    private boolean isInSameDomain(final String url) {
        return url.contains(domain);
    }
}
