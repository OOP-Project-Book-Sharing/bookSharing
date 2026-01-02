package com.example.first_draft.chat;

import com.example.first_draft.BookDatabase;
import com.example.first_draft.UserDatabase;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ChatPageController {
    @FXML private VBox chatBox;
    @FXML private ScrollPane chatScrollPane;
    @FXML private TextField messageField;
    @FXML private Button sendButton;
    @FXML private ListView<String> userList;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private String currentChatUser = "";
    private Set<String> displayedMessages = new HashSet<>(); // Track displayed messages to prevent duplicates

    @FXML
    private void initialize() {
        sendButton.setDisable(false);
        messageField.setDisable(false);

        userList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selectedUser) -> {
            if (selectedUser != null && !selectedUser.trim().isEmpty()) {
                currentChatUser = selectedUser;
                loadChatWith(selectedUser);
            }
        });

        sendButton.setOnAction(e -> sendMessage());
        messageField.setOnAction(e -> sendMessage());
    }

    public void setUsername(String currentUser) {
        this.username = currentUser;
        loadAllChatUsers();
        connectToServer();
    }

    private void loadAllChatUsers() {
        Set<String> allUsers = new HashSet<>();

        // Load users from BookDatabase
        BookDatabase bookDb = new BookDatabase();
        UserDatabase userDb = new UserDatabase();
        List<String> allDbUsers = userDb.getAllUsernames();
        if (allDbUsers != null) {
            allUsers.addAll(allDbUsers);
        }

        // Load users from chat history folder
        File userFolder = new File("chatlogs" + File.separator + username);
        if (userFolder.exists() && userFolder.isDirectory()) {
            File[] chatFiles = userFolder.listFiles();
            if (chatFiles != null) {
                for (File file : chatFiles) {
                    if (file.getName().endsWith(".dat")) {
                        String otherUser = file.getName().replace(".dat", "");
                        allUsers.add(otherUser);
                    }
                }
            }
        }

        // Update user list
        Platform.runLater(() -> {
            userList.getItems().clear();
            for (String user : allUsers) {
                if (!user.equals(username)) {
                    userList.getItems().add(user);
                }
            }
            userList.getItems().sort(String::compareTo);
        });
    }

    private void loadChatWith(String otherUser) {
        Platform.runLater(() -> {
            chatBox.getChildren().clear();
            displayedMessages.clear();
            if (otherUser == null || otherUser.isEmpty()) return;
        });

        currentChatUser = otherUser;

        List<String> history = ChatLogManager.loadMessages(username, otherUser);

        if (history.isEmpty()) {
            Platform.runLater(() -> {
                Label noChatLabel = new Label("No chat with this user");
                noChatLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #999; -fx-font-style: italic;");
                VBox messageContainer = new VBox(noChatLabel);
                messageContainer.setAlignment(Pos.CENTER);
                messageContainer.setPadding(new Insets(50));
                chatBox.getChildren().add(messageContainer);
            });
            return;
        }

        for (String msg : history) {
            boolean isMe = msg.startsWith("(Me →");
            // Extract just the message content, removing the prefix
            String messageContent = msg;
            if (msg.contains("): ")) {
                messageContent = msg.substring(msg.indexOf("): ") + 3);
            }

            // Create unique key for this message
            String messageKey = (isMe ? "ME:" : "THEM:") + messageContent;

            // Only add if not already displayed
            if (!displayedMessages.contains(messageKey)) {
                displayedMessages.add(messageKey);
                addMessageBubble(messageContent, isMe);
            }
        }

        // Scroll to bottom after loading
        Platform.runLater(() -> {
            chatScrollPane.layout();
            chatScrollPane.setVvalue(1.0);
        });
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", ChatServer.PORT);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                if (username == null || username.trim().isEmpty()) {
                    socket.close();
                    return;
                }
                out.println(username);

                Platform.runLater(() -> {
                    sendButton.setDisable(false);
                    messageField.setDisable(false);
                });

                String line;
                while ((line = in.readLine()) != null) {
                    if (line.contains("|")) {
                        String[] parts = line.split("\\|", 2);
                        String sender = parts[0];
                        String msgContent = parts[1];

                        // Save received message to chat log
                        ChatLogManager.saveMessage(sender, username, msgContent, false);

                        // Check if sender is in the user list, if not add them
                        boolean senderExists = false;
                        for (String item : userList.getItems()) {
                            if (item.equals(sender)) {
                                senderExists = true;
                                break;
                            }
                        }

                        if (!senderExists && !sender.equals(username)) {
                            Platform.runLater(() -> loadAllChatUsers());
                        }

                        // Display if chatting with this user
                        if (sender.equals(currentChatUser)) {
                            Platform.runLater(() -> clearNoChatMessage());

                            String messageKey = "THEM:" + msgContent;
                            if (!displayedMessages.contains(messageKey)) {
                                displayedMessages.add(messageKey);
                                addMessageBubble(msgContent, false);
                            }
                        }
                    } else if (line.startsWith("USER_LIST:")) {
                        String csv = line.substring("USER_LIST:".length());
                        updateUserList(csv);
                    }
                }
            } catch (IOException e) {
                Platform.runLater(() -> {
                    System.out.println("Unable to connect to chat server. Working in offline mode.");
                });
            }
        }).start();
    }

    private void updateUserList(String csvNames) {
        if (csvNames == null) csvNames = "";
        String[] onlineNames = csvNames.split(",");
        Set<String> onlineSet = new HashSet<>();
        for (String name : onlineNames) {
            if (name != null && !name.trim().isEmpty() && !"null".equalsIgnoreCase(name)) {
                onlineSet.add(name.trim());
            }
        }

        Platform.runLater(() -> {
            Set<String> allUsers = new HashSet<>();

            // Add users from existing list (preserve chat history)
            for (String item : userList.getItems()) {
                allUsers.add(item.trim());
            }

            // Add users from chat history folder
            File userFolder = new File("chatlogs" + File.separator + username);
            if (userFolder.exists() && userFolder.isDirectory()) {
                File[] chatFiles = userFolder.listFiles();
                if (chatFiles != null) {
                    for (File file : chatFiles) {
                        if (file.getName().endsWith(".dat")) {
                            String otherUser = file.getName().replace(".dat", "");
                            allUsers.add(otherUser);
                        }
                    }
                }
            }

            allUsers.addAll(onlineSet);

            allUsers.remove(username);

            userList.getItems().clear();

            // Add users to list without online indicator
            List<String> sortedUsers = new ArrayList<>(allUsers);
            sortedUsers.sort(String::compareTo);

            for (String user : sortedUsers) {
                userList.getItems().add(user);
            }
        });
    }

    private void clearNoChatMessage() {
        if (chatBox.getChildren().size() == 1) {
            javafx.scene.Node firstChild = chatBox.getChildren().get(0);
            if (firstChild instanceof VBox) {
                VBox vbox = (VBox) firstChild;
                if (!vbox.getChildren().isEmpty() && vbox.getChildren().get(0) instanceof Label) {
                    Label label = (Label) vbox.getChildren().get(0);
                    if ("No chat with this user".equals(label.getText())) {
                        chatBox.getChildren().clear();
                    }
                }
            }
        }
    }

    @FXML
    private void sendMessage() {
        String message = messageField.getText().trim();
        if (currentChatUser == null || currentChatUser.isEmpty() || message.isEmpty()) return;

        String messageKey = "ME:" + message;
        displayedMessages.add(messageKey);

        clearNoChatMessage();

        addMessageBubble(message, true);
        messageField.clear();

        // Send to server if connected - server will save it
        if (out != null) {
            out.println("@" + currentChatUser + " " + message);
            // Server saves it, so we don't save locally
        } else {
            // Only in offline mode, save locally
            ChatLogManager.saveMessage(username, currentChatUser, message, true);
        }
    }

    private void addMessageBubble(String messageText, boolean isSentByMe) {
        Platform.runLater(() -> {
            VBox messageContainer = new VBox(3);
            messageContainer.setPadding(new Insets(5, 10, 5, 10));

            // Message bubble
            Label messageLabel = new Label(messageText);
            messageLabel.setWrapText(true);
            messageLabel.setMaxWidth(400);
            messageLabel.setPadding(new Insets(10, 14, 10, 14));
            messageLabel.setStyle(
                    "-fx-background-radius: 18px; " +
                            "-fx-font-size: 14px; " +
                            "-fx-line-spacing: 2px;"
            );

            if (isSentByMe) {
                messageLabel.setStyle(messageLabel.getStyle() +
                        "-fx-background-color: #DCF8C6; " +
                        "-fx-text-fill: #000000;");
                messageContainer.setAlignment(Pos.CENTER_RIGHT);
            } else {
                messageLabel.setStyle(messageLabel.getStyle() +
                        "-fx-background-color: #FFFFFF; " +
                        "-fx-text-fill: #000000; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 2, 0, 0, 1);");
                messageContainer.setAlignment(Pos.CENTER_LEFT);
            }

            messageContainer.getChildren().add(messageLabel);
            chatBox.getChildren().add(messageContainer);

            // Force auto-scroll to bottom with multiple attempts
            Platform.runLater(() -> {
                chatScrollPane.layout();
                chatScrollPane.setVvalue(1.0);
            });

            new Thread(() -> {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
            }).start();
        });
    }
}
