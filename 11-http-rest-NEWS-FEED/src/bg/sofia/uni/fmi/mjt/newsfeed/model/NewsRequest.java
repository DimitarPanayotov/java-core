package bg.sofia.uni.fmi.mjt.newsfeed.model;

import bg.sofia.uni.fmi.mjt.newsfeed.util.UrlBuilder;

import java.util.Objects;

public class NewsRequest {
    private final String keyword;
    private final String category;
    private final String country;
    private final int page;
    private final int pageSize;

    private NewsRequest(Builder b) {
        this.keyword = b.keyword;
        this.category = b.category;
        this.country = b.country;
        this.page = b.page;
        this.pageSize = b.pageSize;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toUrl(String baseUrl) {
        return new UrlBuilder(baseUrl)
            .addParam("q", keyword)
            .addParam("category", category)
            .addParam("country", country)
            .addParam("page", page > 0 ? String.valueOf(page) : null)
            .addParam("pageSize", pageSize > 0 ? String.valueOf(pageSize) : null)
            .build();
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewsRequest)) return false;
        NewsRequest that = (NewsRequest) o;
        return page == that.page &&
            pageSize == that.pageSize &&
            Objects.equals(keyword, that.keyword) &&
            Objects.equals(category, that.category) &&
            Objects.equals(country, that.country);
    }

    @Override public int hashCode() {
        return Objects.hash(keyword, category, country, page, pageSize);
    }

    public static class Builder {
        private String keyword;
        private String category;
        private String country;
        private int page = 1;
        private int pageSize = 20;

        public Builder keyword(String keyword) {
            this.keyword = keyword;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder page(int page) {
            this.page = page;
            return this;
        }

        public Builder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public NewsRequest build() {
            if (keyword == null || keyword.isBlank()) {
                throw new IllegalArgumentException("Keyword (q) is required");
            }
            if (page <= 0) throw new IllegalArgumentException("page must be >= 1");
            if (pageSize <= 0 || pageSize > 100) {
                throw new IllegalArgumentException("pageSize must be in 1..100");
            }
            return new NewsRequest(this);
        }
    }
}
