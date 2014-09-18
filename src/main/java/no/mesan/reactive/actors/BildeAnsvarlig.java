package no.mesan.reactive.actors;

import java.util.HashSet;
import java.util.Set;

import no.mesan.reactive.actors.messages.FoundImage;
import no.mesan.reactive.actors.messages.ImageRequest;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class BildeAnsvarlig extends AbstractActor {

    private final ActorRef klientAnsvarlig;
    private Set<String> foundImages = new HashSet<>();

    public BildeAnsvarlig(final ActorRef klientAnsvarlig) {
        this.klientAnsvarlig = klientAnsvarlig;

        receive(ReceiveBuilder
                        .match(FoundImage.class, this::handleFoundImage)
                        .matchAny(this::unhandled)
                        .build()
        );
    }

    private void handleFoundImage(final FoundImage foundImage) {
        final String imageUrl = foundImage.getImageUrl();
        if (isANewImage(imageUrl)) {
            foundImages.add(imageUrl);
            final ActorRef bildeLager = context().actorOf(Props.create(BildeLager.class, klientAnsvarlig));
            bildeLager.tell(new ImageRequest(foundImage.getImageUrl()), context().self());
        }
    }

    private boolean isANewImage(final String imageUrl) {
        return !foundImages.contains(imageUrl);
    }
}
