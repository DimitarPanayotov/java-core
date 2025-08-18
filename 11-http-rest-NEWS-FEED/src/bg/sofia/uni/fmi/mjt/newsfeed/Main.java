package bg.sofia.uni.fmi.mjt.newsfeed;

import bg.sofia.uni.fmi.mjt.newsfeed.api.NewsApiClient;
import bg.sofia.uni.fmi.mjt.newsfeed.exception.ApiException;
import bg.sofia.uni.fmi.mjt.newsfeed.model.Article;
import bg.sofia.uni.fmi.mjt.newsfeed.model.NewsRequest;
import bg.sofia.uni.fmi.mjt.newsfeed.model.NewsResponse;

public class Main {
    public static void main(String[] args) {
        String apiKey = System.getenv("NEWS_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Моля, задайте NEWS_API_KEY environment variable!");
            return;
        }

        NewsApiClient client = new NewsApiClient(apiKey);

        NewsRequest request = NewsRequest.builder()
            .keyword("football")
            .page(1)
            .pageSize(5)
            .build();

        try {
            NewsResponse response = client.getTopHeadlines(request);
            System.out.println("Total results: " + response.getTotalResults());
            System.out.println("Showing first " + response.getArticles().size() + " articles:\n");

            for (Article article : response.getArticles()) {
                System.out.println("Title: " + article.getTitle());
                System.out.println("Description: " + article.getDescription());
                System.out.println("URL: " + article.getUrl());
                System.out.println("Published at: " + article.getPublishedAt());
                System.out.println("---");
            }

        } catch (ApiException e) {
            System.err.println("API error: " + e.getMessage());
        }
    }
}
