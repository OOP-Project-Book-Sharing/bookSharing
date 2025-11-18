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
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            username = in.readLine();

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

                        // Send to target
                        ClientHandler targetHandler = ChatServer.clients.get(target);
                        if (targetHandler != null) {
                            targetHandler.out.println(username + "|" + privateMsg);
                        }

                        // Save messages once per perspective
                        ChatLogManager.saveMessage(username, target, privateMsg, true);  // sender
                        ChatLogManager.saveMessage(username, target, privateMsg, false); // receiver
                    }
                }
            }

        } catch (IOException e) {
            System.out.println(username + " disconnected.");
        } finally {
            synchronized (ChatServer.clients) {
                ChatServer.clients.remove(username);
            }
            ChatServer.sendUserListToAll();
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}
