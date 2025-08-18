package bg.sofia.uni.fmi.mjt.poll.server;

import bg.sofia.uni.fmi.mjt.poll.server.repository.InMemoryPollRepository;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

public class MainServer {
    public static void main(String[] args) {
        PollRepository pollRepository = new InMemoryPollRepository();
        PollServer server = new PollServer(4444, pollRepository);
        server.start();
    }
}
