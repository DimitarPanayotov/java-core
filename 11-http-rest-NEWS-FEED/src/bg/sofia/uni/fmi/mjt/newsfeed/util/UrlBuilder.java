package bg.sofia.uni.fmi.mjt.newsfeed.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlBuilder {
    private final StringBuilder sb;
    private boolean firstParam = true;

    public UrlBuilder(String baseUrl) {
        this.sb = new StringBuilder(baseUrl);
    }

    public UrlBuilder addParam(String key, String value) {
        if (value == null || value.isBlank()) return this;
        sb.append(firstParam ? "?" : "&");
        firstParam = false;
        try {
            sb.append(URLEncoder.encode(key, StandardCharsets.UTF_8.toString()))
                .append("=")
                .append(URLEncoder.encode(value, StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public String build() {
        return sb.toString();
    }
}

