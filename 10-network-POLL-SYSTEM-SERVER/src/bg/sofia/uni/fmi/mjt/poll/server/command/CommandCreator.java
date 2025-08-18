package bg.sofia.uni.fmi.mjt.poll.server.command;

public class CommandCreator {
    public static Command newCommand(String clientInput) {
        String trimmedInput = clientInput.trim();
        String[] tokens = trimmedInput.split("\\s+");
        String command = tokens[0];
        String[] args = new String[tokens.length - 1];
        System.arraycopy(tokens, 1, args, 0, tokens.length - 1);

        return new Command(command, args);
    }
}
