package no.mesan.reactive.actors;

import java.io.IOException;

import no.mesan.reactive.actors.messages.FoundImage;
import no.mesan.reactive.actors.messages.ScanMessage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

public class SideLeser extends AbstractActor {

    public static final int TI_SEKUNDER = 10000;
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
        final Document document = Jsoup.connect(url).timeout(TI_SEKUNDER).get();
        document.select("a[href]")
                .stream()
                .map((link) -> link.attr("abs:href"))
                .filter((link) -> link.startsWith("http")) // skip mailTo etc.
                .forEach((link) -> context().parent().tell(new ScanMessage(link), context().self()));
        document.select("img[src]")
                .stream()
                .map((image) -> image.attr("abs:src"))
                .forEach((imageUrl) -> bildeAnsvarlig.tell(new FoundImage(imageUrl), context().self()));
    }
}
