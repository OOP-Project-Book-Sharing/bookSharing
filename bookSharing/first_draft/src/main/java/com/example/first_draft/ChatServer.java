package com.example.first_draft;

import java.io.IOException;
import java.net.BindException; // 1. Import this
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    static final int PORT = 5000;
    private static ServerSocket serverSocket;
    static final Map<String, ClientHandler> clients = new HashMap<>();

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Chat server started on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                new Thread(clientHandler).start();
            }
        } catch (BindException e) {
            System.err.println("Error: Port " + PORT + " is already occupied!");
            System.err.println("Please stop the other server or change the PORT number.");
            System.exit(1); // Exit the program safely

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException ignored) {}
            }
        }
    }

    static void sendUserListToAll() {
        synchronized (clients) {
            StringBuilder list = new StringBuilder("USER_LIST:");
            for (String user : clients.keySet()) {
                list.append(user).append(",");
            }
            String finalList = list.toString();
            for (ClientHandler c : clients.values()) {
                if (c != null && c.out != null) {
                    c.out.println(finalList);
                }
            }
        }
    }
}