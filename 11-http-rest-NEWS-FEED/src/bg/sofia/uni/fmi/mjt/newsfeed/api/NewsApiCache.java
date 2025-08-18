package bg.sofia.uni.fmi.mjt.newsfeed.api;

import bg.sofia.uni.fmi.mjt.newsfeed.model.NewsRequest;
import bg.sofia.uni.fmi.mjt.newsfeed.model.NewsResponse;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class NewsApiCache {
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(5);

    private final Map<NewsRequest, CacheEntry> map = new ConcurrentHashMap<>();
    private final Duration ttl;

    public NewsApiCache() {
        this(DEFAULT_TTL);
    }

    public NewsApiCache(Duration ttl) {
        this.ttl = Objects.requireNonNull(ttl);
    }

    public boolean contains(NewsRequest req) {
        CacheEntry e = map.get(req);
        return e != null && !e.isExpired(ttl);
    }

    public NewsResponse get(NewsRequest req) {
        CacheEntry e = map.get(req);
        return (e == null || e.isExpired(ttl)) ? null : e.value;
    }

    public void put(NewsRequest req, NewsResponse resp) {
        map.put(req, new CacheEntry(resp));
    }

    private static class CacheEntry {
        final NewsResponse value;
        final Instant created = Instant.now();

        CacheEntry(NewsResponse value) {
            this.value = value;
        }

        boolean isExpired(Duration ttl) {
            return Instant.now().isAfter(created.plus(ttl));
        }
    }
}

