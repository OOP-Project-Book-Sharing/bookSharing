package com.example.first_draft;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChatLogManager {

    private static final String FOLDER = "chatlogs";

    static {
        File dir = new File(FOLDER);
        if (!dir.exists()) dir.mkdirs();
    }

    private static File getUserFolder(String username) {
        File userFolder = new File(FOLDER + "/" + username);
        if (!userFolder.exists()) userFolder.mkdirs();
        return userFolder;
    }

    private static File getChatFile(String user, String otherUser) {
        File userFolder = getUserFolder(user);
        return new File(userFolder, otherUser + ".dat");
    }

    // Save message from sender to receiver perspective
    public static synchronized void saveMessage(String sender, String receiver, String message, boolean isFromSender) {
        try {
            File file = isFromSender ? getChatFile(sender, receiver) : getChatFile(receiver, sender);
            try (DataOutputStream out = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(file, true))
            )) {
                String formatted = isFromSender
                        ? "(Me → " + receiver + "): " + message
                        : "(" + sender + " → Me): " + message;
                out.writeUTF(formatted);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load messages for a specific user's perspective
    public static List<String> loadMessages(String user, String otherUser) {
        List<String> list = new ArrayList<>();
        File file = getChatFile(user, otherUser);
        if (!file.exists()) return list;

        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {
            while (true) list.add(in.readUTF());
        } catch (EOFException ignore) {}
        catch (IOException e) { e.printStackTrace(); }

        return list;
    }
}
