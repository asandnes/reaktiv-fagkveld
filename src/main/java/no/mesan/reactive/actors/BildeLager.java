package no.mesan.reactive.actors;

import no.mesan.reactive.actors.messages.ImageRequest;
import no.mesan.reactive.actors.messages.ImageResult;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

public class BildeLager extends AbstractActor {

    private final ActorRef klientAnsvarlig;

    public BildeLager(final ActorRef klientAnsvarlig) {
        this.klientAnsvarlig = klientAnsvarlig;
        receive(ReceiveBuilder
          .match(ImageRequest.class, (ImageRequest imageRequest) -> {
              final String imageUrl = imageRequest.getImageUrl();
              this.klientAnsvarlig.tell(new ImageResult(imageUrl, createThumbnailUrl(imageUrl)), context().self());
          }).matchAny(this::unhandled)
          .build());
    }

    private String createThumbnailUrl(final String imageUrl) {
        // Her kunne man på sikt laget et thumbnail-bilde, evt. mellomlagret etc.
        // p.t. så returnerer vi bare den samme url'en
        return imageUrl;
    }
}
