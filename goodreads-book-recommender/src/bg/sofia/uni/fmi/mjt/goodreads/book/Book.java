package bg.sofia.uni.fmi.mjt.goodreads.book;

import java.util.Arrays;
import java.util.List;

public record Book(
    String ID,
    String title,
    String author,
    String description,
    List<String> genres,
    double rating,
    int ratingCount,
    String URL
) {
    public static Book of(String[] tokens) {
        if (tokens == null || tokens.length != 8) {
            throw new IllegalArgumentException("Invalid tokens array for book!");
        }

        String id = tokens[0];
        String title = tokens[1];
        String author = tokens[2];
        String description = tokens[3];

        String genresRaw = tokens[4];
        List<String> genres;

        if (genresRaw.length() <= 2) {
            genres = List.of();
        } else {
            genres = Arrays.stream(
                    genresRaw.substring(1, genresRaw.length() - 1)
                        .split("\\s*,\\s*")
                )
                .map(s -> s.replaceAll("^'(.*)'$", "$1"))
                .toList();
        }

        double rating = Double.parseDouble(tokens[5]);

        String ratingCountStr = tokens[6].replaceAll(",", "");
        int ratingCount = Integer.parseInt(ratingCountStr);

        String url = tokens[7];

        return new Book(id, title, author, description, genres, rating, ratingCount, url);
    }
}
