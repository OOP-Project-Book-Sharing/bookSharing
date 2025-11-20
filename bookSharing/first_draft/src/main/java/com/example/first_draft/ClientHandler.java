package com.example.first_draft;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    public String username;
    public BufferedReader in;
    public PrintWriter out;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            username = in.readLine();
            if (username == null || username.trim().isEmpty()) {
                socket.close();
                return;
            }

            synchronized (ChatServer.clients) {
                ChatServer.clients.put(username, this);
            }
            ChatServer.sendUserListToAll();

            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("@")) {
                    int space = message.indexOf(" ");
                    if (space != -1) {
                        String target = message.substring(1, space);
                        String privateMsg = message.substring(space + 1);


                        ClientHandler targetHandler;
                        synchronized (ChatServer.clients) {
                            targetHandler = ChatServer.clients.get(target);
                        }
                        if (targetHandler != null && targetHandler.out != null) {
                            targetHandler.out.println(username + "|" + privateMsg);
                        }

                        ChatLogManager.saveMessage(username, target, privateMsg, true);  // sender's perspective
                        ChatLogManager.saveMessage(username, target, privateMsg, false); // receiver's perspective
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(username + " disconnected.");
        } finally {
            synchronized (ChatServer.clients) {
                if (username != null) ChatServer.clients.remove(username);
            }
            ChatServer.sendUserListToAll();
            try {
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException ignored) {}
        }
    }
}
