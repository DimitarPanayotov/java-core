package bg.sofia.uni.fmi.mjt.poll.server.repository;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryPollRepository implements PollRepository {
    private final Map<Integer, Poll> polls;
    private final AtomicInteger idGenerator;

    public InMemoryPollRepository() {
        this.polls = new HashMap<>();
        this.idGenerator = new AtomicInteger(0);
    }

    @Override
    public int addPoll(Poll poll) {
        int id = idGenerator.incrementAndGet();
        polls.put(id, poll);
        return id;
    }

    @Override
    public Poll getPoll(int pollId) {
        return polls.get(pollId);
    }

    @Override
    public Map<Integer, Poll> getAllPolls() {
        return polls;
    }

    @Override
    public void clearAllPolls() {
        polls.clear();
    }

}
