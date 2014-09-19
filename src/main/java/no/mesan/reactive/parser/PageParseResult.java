package no.mesan.reactive.parser;

import java.util.Collection;

public class PageParseResult {

    private final Collection<String> links;
    private final Collection<String> images;

    public PageParseResult(final Collection<String> links, final Collection<String> images) {
        this.links = links;
        this.images = images;
    }

    public Collection<String> getLinks() {
        return links;
    }

    public Collection<String> getImages() {
        return images;
    }
}
