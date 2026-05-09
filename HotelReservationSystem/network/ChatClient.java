package HotelReservationSystem.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

public class ChatClient {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private Consumer<String> messageReceiver;

    public ChatClient(String host, int port, Consumer<String> messageReceiver) throws IOException {
        this.messageReceiver = messageReceiver;

        socket = new Socket(host, port);

        reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );

        writer = new PrintWriter(socket.getOutputStream(), true);

        startListening();
    }

    private void startListening() {
        Thread listenerThread = new Thread(() -> {
            try {
                String message;

                while ((message = reader.readLine()) != null) {
                    messageReceiver.accept(message);
                }

            } catch (IOException e) {
                messageReceiver.accept("Disconnected from chat server.");
            }
        });

        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void sendMessage(String message) {
        if (writer != null) {
            writer.println(message);
        }
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing chat client: " + e.getMessage());
        }
    }
}