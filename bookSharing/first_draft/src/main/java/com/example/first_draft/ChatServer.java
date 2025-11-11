package com.example.first_draft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    private static final int PORT = 5000;
    private static Map<String, ClientHandler> clients = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("📡 Chat server started on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Nested class handles each client
    static class ClientHandler implements Runnable {
        private Socket socket;
        private String username;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Step 1: Login
                out.println("Enter username:");
                username = in.readLine();

                synchronized (clients) {
                    if (clients.containsKey(username)) {
                        out.println("Username already taken. Connection closed.");
                        socket.close();
                        return;
                    }
                    clients.put(username, this);
                }

                System.out.println(username + " joined the chat.");
                broadcast(username + " joined the chat.", null);

                // Step 2: Listen for messages
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("@")) {
                        // private message format: @username message
                        int space = message.indexOf(' ');
                        if (space != -1) {
                            String targetUser = message.substring(1, space);
                            String privateMsg = message.substring(space + 1);
                            sendPrivateMessage(targetUser, privateMsg);
                        }
                    } else {
                        broadcast(username + ": " + message, this);
                    }
                }

            } catch (IOException e) {
                System.out.println(username + " disconnected.");
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
                synchronized (clients) {
                    clients.remove(username);
                }
                broadcast(username + " left the chat.", null);
            }
        }

        private void sendPrivateMessage(String targetUser, String message) {
            ClientHandler target = clients.get(targetUser);
            if (target != null) {
                target.out.println("(Private) " + username + ": " + message);
            } else {
                out.println("User " + targetUser + " not found.");
            }
        }

        private void broadcast(String message, ClientHandler exclude) {
            synchronized (clients) {
                for (ClientHandler client : clients.values()) {
                    if (client != exclude) {
                        client.out.println(message);
                    }
                }
            }
        }
    }
}

