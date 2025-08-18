package bg.sofia.uni.fmi.mjt.newsfeed.model;

public class Article {
    public static class Source {
        public String id;
        public String name;
    }

    private Source source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt; // ISO8601
    private String content;

    public Source getSource() {
        return source;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }
}
