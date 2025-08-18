package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    @SuppressWarnings("checkstyle:MethodLength")
    public static void main(String[] args) {
        Map<String, SentimentScore> lexicon = readLexicon("AFINN-111.txt");

        Set<String> stopWords = readStopWords("stopwords.txt");

        try {
            AnalyzerInput fileInput1 = new AnalyzerInput("input1", new FileReader("input1.txt"));
            AnalyzerInput fileInput2 = new AnalyzerInput("input2", new FileReader("input2.txt"));

            ParallelSentimentAnalyzer fileAnalyzer = new ParallelSentimentAnalyzer(2, stopWords, lexicon);

            Map<String, SentimentScore> fileResults = fileAnalyzer.analyze(fileInput1, fileInput2);

            System.out.println("File Analysis Results:");
            fileResults.forEach((id, score) ->
                System.out.printf("%s: %s (Score: %d)%n", id, score.getDescription(), score.getScore()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Map<String, SentimentScore> readLexicon(String path) {
        Map<String, SentimentScore> lexicon = new HashMap<>();
        try (BufferedReader br = Files.newBufferedReader(Path.of(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.strip().split("\\s+");
                if (tokens.length == 2) {
                    String word = tokens[0].toLowerCase();
                    double score = Double.parseDouble(tokens[1]);
                    lexicon.put(word, SentimentScore.fromScore((int) Math.round(score)));
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read lexicon: " + e.getMessage());
        }
        return lexicon;
    }

    private static Set<String> readStopWords(String path) {
        Set<String> stopWords = new HashSet<>();
        try {
            List<String> lines = Files.readAllLines(Path.of(path));
            for (String line : lines) {
                stopWords.add(line.strip().toLowerCase());
            }
        } catch (IOException e) {
            System.out.println("Could not read stopwords: " + e.getMessage());
        }
        return stopWords;
    }

    private static Reader getReader(String fileName) {
        try {
            return new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Could not read file: " + fileName);
            return new StringReader("");
        }
    }
}
