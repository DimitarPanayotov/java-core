package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class Producer extends Thread {
    private final AnalyzerInput input;
    private final List<Task> taskList;
    private final Object lock;
    private final Counter finishedProducers;

    public Producer(AnalyzerInput input, List<Task> taskList, Object lock, Counter finishedProducers) {
        this.input = input;
        this.taskList = taskList;
        this.lock = lock;
        this.finishedProducers = finishedProducers;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(input.inputReader())) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(" ");
            }

            synchronized (lock) {
                taskList.add(new Task(input.inputID(), content.toString()));
                lock.notifyAll();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            synchronized (lock) {
                finishedProducers.increment();
                lock.notifyAll();
            }
        }
    }
}
