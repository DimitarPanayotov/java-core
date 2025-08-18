package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.exceptions.SentimentAnalysisException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParallelSentimentAnalyzer implements SentimentAnalyzerAPI {
    private final int workersCount;
    private final Set<String> stopWords;
    private final Map<String, SentimentScore> sentimentLexicon;

    public ParallelSentimentAnalyzer(int workersCount,
                                     Set<String> stopWords,
                                     Map<String, SentimentScore> sentimentLexicon) {
        this.workersCount = workersCount;
        this.stopWords = stopWords;
        this.sentimentLexicon = sentimentLexicon;
    }

    @Override
    public Map<String, SentimentScore> analyze(AnalyzerInput... input) throws SentimentAnalysisException {
        List<Task> taskList = new ArrayList<>();
        Map<String, SentimentScore> resultMap = new HashMap<>();
        Object lock = new Object();
        Counter finishedProducers = new Counter();

        List<Thread> producers = new ArrayList<>();
        for (AnalyzerInput in : input) {
            Thread p = new Producer(in, taskList, lock, finishedProducers);
            producers.add(p);
            p.start();
        }

        List<Thread> consumers = new ArrayList<>();
        for (int i = 0; i < workersCount; i++) {
            Thread c = new Consumer(taskList, resultMap, lock,
                stopWords, sentimentLexicon, input.length, finishedProducers);
            consumers.add(c);
            c.start();
        }

        for (Thread p : producers) {
            try {
                p.join();
            } catch (InterruptedException e) {
                throw new SentimentAnalysisException("Producer thread interrupted", e);
            }
        }

        for (Thread c : consumers) {
            try {
                c.join();
            } catch (InterruptedException e) {
                throw new SentimentAnalysisException("Consumer thread interrupted", e);
            }
        }

        return resultMap;
    }
}
