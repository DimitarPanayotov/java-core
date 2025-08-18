package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextTokenizer {
    private final Set<String> stopwords;

    private static final Pattern PUNCTUATION_EXCEPT_APOSTROPHE = Pattern.compile("[\\p{Punct}&&[^']]");

    private static final Pattern WHITESPACE = Pattern.compile("\\s+");

    public TextTokenizer(Reader stopwordsReader) {
        try (var br = new BufferedReader(stopwordsReader)) {
            stopwords = br.lines().collect(Collectors.toSet());
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not load dataset", ex);
        }
    }

    public List<String> tokenize(String input) {
        if (input == null || input.isBlank()) {
            return List.of();
        }

        String lowerCased = input.toLowerCase();

        String noPunct = PUNCTUATION_EXCEPT_APOSTROPHE.matcher(lowerCased).replaceAll(" ");

        String cleaned = WHITESPACE.matcher(noPunct).replaceAll(" ").trim();

        String[] tokens = WHITESPACE.split(cleaned);

        List<String> result = new ArrayList<>();
        for (String token : tokens) {
            token = token.replace("'", "");

            if (!stopwords.contains(token) && !token.isEmpty()) {
                result.add(token);
            }
        }

        return result;
    }

    public Set<String> stopwords() {
        return stopwords;
    }
}
