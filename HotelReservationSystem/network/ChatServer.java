package HotelReservationSystem.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

    private static final int PORT = 5000;
    private static final ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Chat server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {
                Socket socket = serverSocket.accept();

                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);

                Thread thread = new Thread(clientHandler);
                thread.start();

                System.out.println("New client connected.");
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private static void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    private static void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        System.out.println("Client disconnected.");
    }

    private static class ClientHandler implements Runnable {

        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(Socket socket) {
            this.socket = socket;

            try {
                reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );

                writer = new PrintWriter(socket.getOutputStream(), true);

            } catch (IOException e) {
                System.out.println("Client setup error: " + e.getMessage());
            }
        }

        @Override
        public void run() {
            try {
                String message;

                while ((message = reader.readLine()) != null) {
                    System.out.println("Received: " + message);
                    broadcastMessage(message, this);
                }

            } catch (IOException e) {
                System.out.println("Client error: " + e.getMessage());

            } finally {
                removeClient(this);

                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Socket close error: " + e.getMessage());
                }
            }
        }

        public void sendMessage(String message) {
            writer.println(message);
        }
    }
}