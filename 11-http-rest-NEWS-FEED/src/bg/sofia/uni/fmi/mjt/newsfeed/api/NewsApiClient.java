package bg.sofia.uni.fmi.mjt.newsfeed.api;

import bg.sofia.uni.fmi.mjt.newsfeed.exception.ApiException;
import bg.sofia.uni.fmi.mjt.newsfeed.model.NewsRequest;
import bg.sofia.uni.fmi.mjt.newsfeed.model.NewsResponse;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NewsApiClient {

    private static final String BASE_URL = "https://newsapi.org/v2/top-headlines";

    private final String apiKey;
    private final HttpClientWrapper http;
    private final NewsApiCache cache;
    private final Gson gson = new Gson();

    public NewsApiClient(String apiKey) {
        this(apiKey, new HttpClientWrapper(), new NewsApiCache());
    }

    public NewsApiClient(String apiKey, HttpClientWrapper http, NewsApiCache cache) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API key must be provided (non-empty)");
        }
        this.apiKey = apiKey;
        this.http = Objects.requireNonNull(http, "http");
        this.cache = Objects.requireNonNull(cache, "cache");
    }

    public static NewsApiClient fromEnv(String envVar) {
        String key = System.getenv(envVar);
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Missing API key in env var: " + envVar);
        }
        return new NewsApiClient(key);
    }

    public NewsResponse getTopHeadlines(NewsRequest request) throws ApiException {
        if (cache.contains(request)) {
            return cache.get(request);
        }

        String url = request.toUrl(BASE_URL);
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Api-Key", apiKey);

        HttpResponse httpResponse = http.get(url, headers);
        NewsResponse parsed = ResponseHandler.handle(httpResponse, gson);
        cache.put(request, parsed);
        return parsed;
    }
}

