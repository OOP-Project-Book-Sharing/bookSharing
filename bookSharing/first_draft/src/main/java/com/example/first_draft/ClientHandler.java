package com.example.first_draft;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    protected String username;
    protected BufferedReader in;
    protected PrintWriter out;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // First line must be the username (client responsibility)
            username = in.readLine();
            if (username == null || username.trim().isEmpty()) {
                // invalid username -> close
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

                        // Send to target ClientHandler (if online)
                        ClientHandler targetHandler;
                        synchronized (ChatServer.clients) {
                            targetHandler = ChatServer.clients.get(target);
                        }
                        if (targetHandler != null && targetHandler.out != null) {
                            targetHandler.out.println(username + "|" + privateMsg);
                        }

                        // Save messages once per perspective on server side:
                        ChatLogManager.saveMessage(username, target, privateMsg, true);  // sender's perspective
                        ChatLogManager.saveMessage(username, target, privateMsg, false); // receiver's perspective
                    }
                }
                // (Optionally handle more commands here)
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
