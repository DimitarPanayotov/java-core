package bg.sofia.uni.fmi.mjt.poll.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class PollClient {
    private static final int SERVER_PORT = 4444;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 1024;
    private static final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public static void main(String[] args) {
        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.configureBlocking(true);
            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            System.out.println("Connected to the poll server. Type commands or 'disconnect' to exit.");

            while (true) {
                System.out.print("> ");
                String command = scanner.nextLine();

                if ("disconnect".equalsIgnoreCase(command.trim())) {
                    break;
                }

                sendCommand(socketChannel, command);

                String response = readResponse(socketChannel);
                if (response != null) {
                    System.out.println("Server response: " + response);
                } else {
                    System.out.println("Server closed connection");
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void sendCommand(SocketChannel channel, String command) throws IOException {
        buffer.clear();
        buffer.put(command.getBytes());
        buffer.flip();
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }

    private static String readResponse(SocketChannel channel) throws IOException {
        buffer.clear();
        int bytesRead = channel.read(buffer);
        if (bytesRead == -1) {
            return null;
        }

        buffer.flip();
        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);
        return new String(byteArray);
    }
}
