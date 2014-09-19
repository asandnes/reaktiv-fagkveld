package no.mesan.reactive.parser;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public final class PageParserUtils {

    public static final int TI_SEKUNDER = 10000;

    private PageParserUtils() {
    }

    public static PageParseResult parseUrl(final String url) throws IOException {
        final Document document = Jsoup.connect(url).timeout(TI_SEKUNDER).get();
        final Set<String> links = document.select("a[href]")
                .stream()
                .map((link) -> link.attr("abs:href"))
                .filter((link) -> link.startsWith("http")) // skip mailTo etc.
                .collect(Collectors.toSet());
        final Set<String> images = document.select("img[src]")
                .stream()
                .map((image) -> image.attr("abs:src"))
                .collect(Collectors.toSet());
        return new PageParseResult(links, images);
    }
}
