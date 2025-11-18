package com.example.first_draft;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    static final int PORT = 5000;
    private static ServerSocket serverSocket;
    protected static final Map<String, ClientHandler> clients = new HashMap<>();

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Chat server started on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                new Thread(clientHandler).start();
            }
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

    protected static void sendUserListToAll() {
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
