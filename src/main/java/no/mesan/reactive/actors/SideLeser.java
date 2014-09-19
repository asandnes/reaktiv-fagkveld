package no.mesan.reactive.actors;

import java.io.IOException;

import no.mesan.reactive.actors.messages.FoundImage;
import no.mesan.reactive.actors.messages.ScanMessage;
import no.mesan.reactive.parser.PageParseResult;
import no.mesan.reactive.parser.PageParserUtils;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

public class SideLeser extends AbstractActor {

    private final ActorRef bildeAnsvarlig;

    public SideLeser(final ActorRef bildeAnsvarlig) {
        this.bildeAnsvarlig = bildeAnsvarlig;
        receive(ReceiveBuilder
                        .match(ScanMessage.class, this::handleScanMessage)
                        .matchAny(this::unhandled)
                        .build()
        );
    }

    private void handleScanMessage(final ScanMessage scanMessage) throws IOException {
        final String url = scanMessage.getUrl();
        final PageParseResult pageParseResult = PageParserUtils.parseUrl(url);

        pageParseResult.getLinks().forEach((link) -> {
            context().parent().tell(new ScanMessage(link), context().self());
        });
        pageParseResult.getImages().forEach((imageUrl) -> {
            bildeAnsvarlig.tell(new FoundImage(imageUrl), context().self());
        });
    }
}
