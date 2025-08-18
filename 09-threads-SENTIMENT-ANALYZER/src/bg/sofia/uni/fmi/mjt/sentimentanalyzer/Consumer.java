package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Consumer extends Thread {
    private final List<Task> taskList;
    private final Map<String, SentimentScore> resultMap;
    private final Object lock;
    private final Set<String> stopWords;
    private final Map<String, SentimentScore> lexicon;
    private final int totalProducers;
    private final Counter finishedProducers;

    public Consumer(List<Task> taskList, Map<String, SentimentScore> resultMap, Object lock,
                    Set<String> stopWords, Map<String, SentimentScore> lexicon,
                    int totalProducers, Counter finishedProducers) {
        this.taskList = taskList;
        this.resultMap = resultMap;
        this.lock = lock;
        this.stopWords = stopWords;
        this.lexicon = lexicon;
        this.totalProducers = totalProducers;
        this.finishedProducers = finishedProducers;
    }

    @Override
    public void run() {
        while (true) {
            Task task = null;

            synchronized (lock) {
                while (taskList.isEmpty() && finishedProducers.get() < totalProducers) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }

                if (!taskList.isEmpty()) {
                    task = taskList.remove(0);
                } else if (finishedProducers.get() == totalProducers) {
                    return;
                }
            }

            if (task != null) {
                analyze(task);
            }
        }
    }

    private void analyze(Task task) {
        String text = task.text().toLowerCase().replaceAll("[^a-zA-Z\\s]", " ");
        String[] words = text.split("\\s+");

        int sum = 0;
        int count = 0;

        for (String word : words) {
            if (word.isBlank() || stopWords.contains(word)) continue;

            SentimentScore score = lexicon.get(word);
            if (score != null) {
                sum += score.getScore();
                count++;
            }
        }

        int avg = (count == 0) ? 0 : sum / count;
        SentimentScore sentiment = SentimentScore.fromScore(avg);

        synchronized (lock) {
            resultMap.put(task.id(), sentiment);
        }
    }
}
