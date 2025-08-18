package bg.sofia.uni.fmi.mjt.newsfeed.api;

import bg.sofia.uni.fmi.mjt.newsfeed.exception.UnauthorizedException;
import bg.sofia.uni.fmi.mjt.newsfeed.model.NewsRequest;
import bg.sofia.uni.fmi.mjt.newsfeed.model.NewsResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NewsApiClientTest {

    private static final String VALID_JSON = """
      {
        "status": "ok",
        "totalResults": 1,
        "articles": [
          {
            "source": {"id": null, "name": "Example"},
            "author": "John Doe",
            "title": "Hello World",
            "description": "Desc",
            "url": "https://example.com",
            "urlToImage": "https://example.com/img.jpg",
            "publishedAt": "2025-08-10T10:00:00Z",
            "content": "Content"
          }
        ]
      }
      """;

    @Test
    void ok_response_is_parsed_and_cached() throws Exception {
        HttpClientWrapper http = mock(HttpClientWrapper.class);
        when(http.get(anyString(), anyMap()))
            .thenReturn(new HttpResponse(200, VALID_JSON));

        NewsApiCache cache = new NewsApiCache();
        NewsApiClient client = new NewsApiClient("KEY", http, cache);

        NewsRequest req = NewsRequest.builder()
            .keyword("java")
            .country("us")
            .page(1)
            .pageSize(10)
            .build();

        NewsResponse r1 = client.getTopHeadlines(req);
        assertEquals("ok", r1.getStatus());
        assertEquals(1, r1.getTotalResults());
        assertNotNull(r1.getArticles());
        assertEquals(1, r1.getArticles().size());

        NewsResponse r2 = client.getTopHeadlines(req);
        assertSame(r1, r2);
        verify(http, times(1)).get(anyString(), anyMap());
    }

    @Test
    void unauthorized_is_mapped_to_exception() throws Exception {
        HttpClientWrapper http = mock(HttpClientWrapper.class);
        when(http.get(anyString(), anyMap()))
            .thenReturn(new HttpResponse(401, """
                  {"status":"error","code":"apiKeyInvalid","message":"API key invalid"}
                """));

        NewsApiClient client = new NewsApiClient("BAD", http, new NewsApiCache());

        NewsRequest req = NewsRequest.builder()
            .keyword("java")
            .build();

        assertThrows(UnauthorizedException.class, () -> client.getTopHeadlines(req));
    }

    @Test
    void request_builder_requires_keyword() {
        assertThrows(IllegalArgumentException.class, () ->
            NewsRequest.builder()
                .country("us")
                .build()
        );
    }

    @Test
    void page_and_pageSize_validation() {
        assertThrows(IllegalArgumentException.class, () ->
            NewsRequest.builder().keyword("x").page(0).build()
        );
        assertThrows(IllegalArgumentException.class, () ->
            NewsRequest.builder().keyword("x").pageSize(0).build()
        );
        assertThrows(IllegalArgumentException.class, () ->
            NewsRequest.builder().keyword("x").pageSize(101).build()
        );
    }
}

