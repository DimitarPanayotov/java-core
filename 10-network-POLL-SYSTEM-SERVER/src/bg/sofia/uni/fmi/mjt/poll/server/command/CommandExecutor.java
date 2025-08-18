package bg.sofia.uni.fmi.mjt.poll.server.command;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private static final String CREATE_POLL = "create-poll";
    private static final String LIST_POLLS = "list-polls";
    private static final String SUBMIT_VOTE = "submit-vote";
    private static final String DISCONNECT = "disconnect";

    private final PollRepository pollRepository;

    public CommandExecutor(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    public String execute(Command cmd) {
        return switch (cmd.command()) {
            case CREATE_POLL -> createPoll(cmd.arguments());
            case LIST_POLLS -> listPolls(cmd.arguments());
            case SUBMIT_VOTE -> submitVote(cmd.arguments());
            case DISCONNECT -> "Disconnected from server.";
            default -> "{\"status\":\"ERROR\",\"message\":\"Unknown command\"}";
        };
    }

    private String createPoll(String[] args) {
        if (args.length < 2) {
            return "{\"status\":\"ERROR\",\"message\":\"Usage: create-poll <question> <option-1> <option-2> [... <option-N>]\"}";
        }

        String question = args[0];
        Map<String, Integer> options = new HashMap<>();
        for (int i = 1; i < args.length; i++) {
            options.put(args[i], 0);
        }

        Poll poll = new Poll(question, options);
        int pollId = pollRepository.addPoll(poll);

        return String.format("{\"status\":\"OK\",\"message\":\"Poll %d created successfully.\"}", pollId);
    }

    private String listPolls(String[] args) {
        Map<Integer, Poll> polls = pollRepository.getAllPolls();

        if (polls.isEmpty()) {
            return "{\"status\":\"ERROR\",\"message\":\"No active polls available.\"}";
        }

        StringBuilder jsonBuilder = new StringBuilder("{\"status\":\"OK\",\"polls\":{");

        polls.forEach((id, poll) -> {
            jsonBuilder.append(String.format("\"%d\":{\"question\":\"%s\",\"options\":{", id, poll.question()));

            boolean firstOption = true;
            for (Map.Entry<String, Integer> option : poll.options().entrySet()) {
                if (!firstOption) {
                    jsonBuilder.append(",");
                }
                jsonBuilder.append(String.format("\"%s\":%d", option.getKey(), option.getValue()));
                firstOption = false;
            }
            jsonBuilder.append("}},");
        });

        if (!polls.isEmpty()) {
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        }

        jsonBuilder.append("}}");
        return jsonBuilder.toString();
    }

    private String submitVote(String[] args) {
        if (args.length != 2 || args[0].isEmpty() || args[1].isEmpty()) {
            return "{\"status\":\"ERROR\",\"message\":\"Usage: submit-vote <poll-id> <option>\"}";
        }

        try {
            int pollId = Integer.parseInt(args[0]);
            String option = args[1];

            Poll poll = pollRepository.getPoll(pollId);

            if (poll == null) {
                return String.format("{\"status\":\"ERROR\",\"message\":\"Poll with ID %d does not exist.\"}", pollId);
            }

            if (!poll.options().containsKey(option)) {
                return String.format("{\"status\":\"ERROR\",\"message\":\"Invalid option. Option %s does not exist.\"}", option);
            }

            poll.options().put(option, poll.options().get(option) + 1);

            return String.format("{\"status\":\"OK\",\"message\":\"Vote submitted successfully for option: %s\"}", option);
        } catch (NumberFormatException e) {
            return "{\"status\":\"ERROR\",\"message\":\"Invalid poll ID. Poll ID must be an integer.\"}";
        }
    }
}
